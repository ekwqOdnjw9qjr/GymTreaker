package ru.fitnes.fitnestreaker.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.config.CustomUserDetails;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        return userMapper.userResponseToDto(user);
    }

    @Override
    public UserResponseDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findUserById(customUserDetails.getId());
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

    @Override
    public UserRequestDto registerNewUser(UserRequestDto userRequestDto){
        if (userRepository.checkEmailExists(userRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email is already register");
        }
        User user = userMapper.userRequestToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        User savedUser = userRepository.save(user);
        return userMapper.userRequestToDto(savedUser);
    }

    @Override
    public UserResponseDto changeRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.userResponseToDto(savedUser);
    }



    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public UserRequestDto update(UserRequestDto userRequestDto, Long id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "User with id: " + id + " not found."));
        userMapper.merge(userToUpdate, userMapper.userRequestToEntity(userRequestDto));
        userToUpdate.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(userToUpdate);
        return userMapper.userRequestToDto(savedUser);
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public void delete(Long id, HttpSession session) {
        userRepository.deleteById(id);
        logout(session);

    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

    }

}
