package ru.fitnes.fitnestreaker.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.entity.enums.Role;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.impl.MailService;
import ru.fitnes.fitnestreaker.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityConfig securityConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
     void testGetUserInfo() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserResponseDto userResponseDto = new UserResponseDto();

        when(securityConfig.getCurrentUser()).thenReturn(new CustomUserDetails(user));
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userMapper.userResponseToDto(user)).thenReturn(userResponseDto);


        UserResponseDto result = userService.getUserInfo();


        assertNotNull(result);
        assertEquals(userResponseDto, result);
        verify(securityConfig).getCurrentUser();
        verify(userRepository).findUserById(userId);
        verify(userMapper).userResponseToDto(user);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userResponseToDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        verify(userRepository).findById(userId);
        verify(userMapper).userResponseToDto(user);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByIdUserNotFound() {

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> userService.getById(userId));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("User with id: 1 not found.", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUser() {

        User firstUser = new User();
        User secondUser = new User();

        UserResponseDto userResponse1 = new UserResponseDto();
        UserResponseDto userResponse2 = new UserResponseDto();

        List<User> userList = List.of(firstUser,secondUser);
        List<UserResponseDto> userResponseList = List.of(userResponse1,userResponse2);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userResponseToListDto(userList)).thenReturn(userResponseList);

        List<UserResponseDto> result = userService.getAll();

        assertNotNull(result);
        assertEquals(userResponseList, result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(userMapper).userResponseToListDto(userList);

    }

    @Test
    void testRegisterNewUser() throws MessagingException {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("qwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .password("zxc")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("qwe@gmail.com");
        user.setFirstName("Alex");
        user.setLastName("Freak");
        user.setPassword("zxc");

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("qwe@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .build();

        when(userMapper.userRequestToEntity(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("zxc");
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(mailService).sendRegistrationMail(user.getEmail(), user.getFirstName(), user.getLastName());
        when(userMapper.userResponseToDto(user)).thenReturn(userResponseDto);


        UserResponseDto result = userService.registerNewUser(userRequestDto);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        verify(userMapper).userRequestToEntity(userRequestDto);
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(user);
        verify(mailService).sendRegistrationMail(user.getEmail(), user.getFirstName(), user.getLastName());
        verify(userMapper).userResponseToDto(user);

    }

    @Test
    void registerUserIfEmailAlreadyExists() throws MessagingException {

        User user = new User();
        user.setId(1L);
        user.setEmail("qwerty@gmail.com");
        user.setFirstName("Alex");
        user.setLastName("Freak");
        user.setPassword("zxc");

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("qwerty@gmail.com")
                .firstName("Alex")
                .lastName("Freak")
                .password("zxc")
                .build();

        when(userRepository.checkEmailExists(userRequestDto.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService
                .registerNewUser(userRequestDto));

        assertEquals("User with this email is already register", exception.getMessage());
        verify(userRepository, never()).save(user);
        verify(mailService, never()).sendRegistrationMail(user.getEmail(),user.getFirstName(),user.getLastName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testChangeRole() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRole(Role.ROLE_USER);

        Role newRole = Role.ROLE_TRAINER;

        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userResponseToDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.changeRole(userId,newRole);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        assertEquals(newRole,user.getRole());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
        verify(userMapper).userResponseToDto(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testChangeRoleIfUserNotFound() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRole(Role.ROLE_USER);

        Role newRole = Role.ROLE_TRAINER;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> userService.changeRole(userId,newRole));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("User with id: 1 not found.", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateUser() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail("qwerty@gmail.com");
        user.setFirstName("Alex");
        user.setLastName("Freak");
        user.setPassword("zxc");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("zxc@gmail.com");
        updatedUser.setFirstName("Yousef");
        updatedUser.setLastName("Legend");
        updatedUser.setPassword("sgsgfsdfdsfdsfsdf");

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("zxc@gmail.com")
                .firstName("Yousef")
                .lastName("Legend")
                .password("zxc")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .email("zxc@gmail.com")
                .firstName("Yousef")
                .lastName("Legend")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userResponseToDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.update(userRequestDto,userId);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        assertEquals(userResponseDto.getEmail(),userRequestDto.getEmail());
        verify(userRepository).findById(userId);
        verify(userMapper).merge(user, userMapper.userRequestToEntity(userRequestDto));
        verify(userRepository).save(user);
        verify(userMapper).userResponseToDto(user);

    }

    @Test
    void updateUserIfUserNotFound() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("zxc@gmail.com")
                .firstName("Yousef")
                .lastName("Legend")
                .password("zxc")
                .build();


        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        LocalException exception = assertThrows(LocalException.class, () -> userService.update(userRequestDto,userId));

        assertEquals(ErrorType.NOT_FOUND, exception.getType());
        assertEquals("User with id: 1 not found.", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void testDeleteUserById() {

        Long userId = 1L;

        HttpSession mockSession = mock(HttpSession.class);

        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId, mockSession);

        verify(userRepository).deleteById(userId);

    }


}
