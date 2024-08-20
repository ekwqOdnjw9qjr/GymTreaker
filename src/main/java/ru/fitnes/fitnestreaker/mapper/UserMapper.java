package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRequestDto userRequestToDto(User user);

    User userRequestToEntity(UserRequestDto userRequestDto);

    UserResponseDto userResponseToDto(User user);

    List<UserResponseDto> userResponseToListDto(List<User> userList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    void merge(@MappingTarget User target, User source);


}
