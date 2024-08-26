package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;

import java.util.List;
import java.util.Set;

public interface SessionService {

    SessionResponseDto getById(Long id);

    List<SessionResponseDto> getYourSessions();

    List<SessionResponseDto> getAll();


    SessionRequestDto create(SessionRequestDto sessionRequestDto);

    SessionCommentRequest addTrainerCommentForSessions(Long id,SessionCommentRequest sessionCommentRequest);

    void changeStatus(Long id, SessionStatus status);


    List<SessionResponseInfo> getSessions();


    void delete(Long id);
}


