package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fitnes.fitnestreaker.config.SecurityConfig;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.Role;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.UserSpecification;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;



    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        return userMapper.userResponseToDto(user);
    }

    @Override
    public UserResponseDto getUserInfoByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (!user.getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new RuntimeException("You do not have permission to check this user's data.");
        }
        return userMapper.userResponseToDto(user);
    }

    @Override
    public List<UserResponseDto> searchUsersByAnyFields(UserRequestDto userRequestDto) {
        Specification<User> spec = Specification.where(UserSpecification.hasFirstName(userRequestDto.getFirstName()))
                .or(UserSpecification.hasLastName(userRequestDto.getLastName()))
                .or(UserSpecification.hasEmail(userRequestDto.getEmail()));
        List<User> userList = userRepository.findAll(spec);
        return userMapper.userResponseToListDto(userList);
    }

    @Override
    public List<UserResponseDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userMapper.userResponseToListDto(userList);
    }

    public UserRequestDto registerNewUser(UserRequestDto userRequestDto) throws Exception {
        System.out.println("Registering user: " + userRequestDto);
        User user = userMapper.userRequestToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        User savedUser = userRepository.save(user);
        System.out.println("User saved: " + savedUser);
        return userMapper.userRequestToDto(savedUser);
    }

    public UserResponseDto changeRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.userResponseToDto(savedUser);
    }

    public UserRequestDto update(UserRequestDto userRequestDto, Long id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "User with id: " + id + " not found."));
        if (!userToUpdate.getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new RuntimeException("You do not have permission to update this user's data.");
        }

        userMapper.merge(userToUpdate, userMapper.userRequestToEntity(userRequestDto));
        userToUpdate.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(userToUpdate);
        return userMapper.userRequestToDto(savedUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
