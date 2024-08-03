package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.UserSpecification;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
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
    @Transactional
    public UserRequestDto create(UserRequestDto userRequestDto) {
        User user = userMapper.userRequestToEntity(userRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.userRequestToDto(savedUser);
    }

    @Override
    @Transactional
    public UserRequestDto update(UserRequestDto dto, Long id) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        User newUser = userMapper.userRequestToEntity(dto);
        userMapper.merge(oldUser, newUser);
        User savedUser = userRepository.save(oldUser);
        return userMapper.userRequestToDto(savedUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
