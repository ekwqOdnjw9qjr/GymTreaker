package ru.fitnes.fitnestreaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.Exception;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userMapper.toListDto(userList);
    }
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }



    @Transactional
    public UserDto update(UserDto dto, Long id) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(()-> new Exception(ErrorType.NOT_FOUND,"User with id: " + id + " not found."));
        User newUser = userMapper.toEntity(dto);
        userMapper.merge(oldUser, newUser);
        User savedUser = userRepository.save(oldUser);
        return userMapper.toDto(savedUser);
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
