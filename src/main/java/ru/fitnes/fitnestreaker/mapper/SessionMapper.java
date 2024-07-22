package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.SessionDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class})
public interface SessionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "trainer.id", target = "trainerId")
    SessionDto toDto(Session session);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "trainerId", target = "trainer.id")
    Session toEntity(SessionDto sessionDto);

    List<SessionDto> toListDto(List<Session> sessionList);

    List<Session> toListEntity(List<SessionDto> sessionDtoList);

    void merge(@MappingTarget Session target, Session source);
}
