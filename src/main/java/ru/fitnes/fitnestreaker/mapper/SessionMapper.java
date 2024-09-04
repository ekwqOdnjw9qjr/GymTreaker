package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.Session;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class, SessionMapper.class})
public interface SessionMapper {

    @Mapping(source = "coachingTime.id", target = "coachingTimeId")
    SessionRequestDto sessionRequestToDto(Session session);

    SessionCommentRequest sessionCommentRequestToDto(Session session);

    @Mapping(source = "coachingTimeId", target = "coachingTime.id" )
    Session sessionRequestToEntity(SessionRequestDto sessionRequestDto);

    SessionResponseDto sessionResponseToDto(Session session);

    List<SessionResponseInfo> sessionResponseInfoToDto(List<Session> session);

    List<SessionResponseDto> sessionResponseToListDto(List<Session> sessionList);


}
