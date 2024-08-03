package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class})
public interface SessionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "trainer.id", target = "trainerId")
    SessionRequestDto sessionRequestToDto(Session session);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "trainerId", target = "trainer.id")
    Session sessionRequestToEntity(SessionRequestDto sessionRequestDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "trainer.id", target = "trainerId")
    SessionResponseDto sessionResponseToDto(Session session);

    List<SessionResponseDto> sessionResponseToListDto(List<Session> sessionList);

    void merge(@MappingTarget Session target, Session source);

//    List<Session> toListEntity(List<SessionRequestDto> sessionRequestDtoList);
//    @Mapping(source = "userId", target = "user.id")
//    @Mapping(source = "trainerId", target = "trainer.id")
//    Session sessionResponseToEntity(SessionResponseDto sessionResponseDto);
//    List<SessionRequestDto> sessionRequestToListDto(List<Session> sessionList);


}
