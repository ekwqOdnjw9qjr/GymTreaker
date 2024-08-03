package ru.fitnes.fitnestreaker.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.repository.UserSpecification;

import java.util.List;

public interface UserService {

     UserResponseDto getById(Long id);

     List<UserResponseDto> searchUsersByAnyFields(UserRequestDto userRequestDto);

     List<UserResponseDto> getAll();

     UserRequestDto create(UserRequestDto userRequestDto);

     UserRequestDto update(UserRequestDto dto, Long id);

     void delete(Long id);

}
