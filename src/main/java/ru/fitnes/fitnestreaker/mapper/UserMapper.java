package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toListDto(List<User> userList);

    List<User> toListEntity(List<UserDto> userDtoList);

    void merge(@MappingTarget User target, User source);
}
