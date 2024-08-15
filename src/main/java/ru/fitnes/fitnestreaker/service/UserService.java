package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.Role;

import java.util.List;

public interface UserService {

     UserResponseDto getById(Long id);

     UserResponseDto getUserInfoByEmail(String email);

     List<UserResponseDto> searchUsersByAnyFields(UserRequestDto userRequestDto);

     List<UserResponseDto> getAll();

     UserRequestDto registerNewUser(UserRequestDto userRequestDto) throws Exception;

     UserRequestDto update(UserRequestDto dto, Long id);

     UserResponseDto changeRole(Long id, Role role);

     void delete(Long id);


}
