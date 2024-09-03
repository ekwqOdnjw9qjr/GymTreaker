package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    SessionResponseDto getById(Long id);

    List<SessionResponseDto> getYourSessions();

    List<SessionResponseDto> getAll();

    SessionResponseDto create(SessionRequestDto sessionRequestDto);

    SessionCommentRequest addTrainerCommentForSessions(Long id,SessionCommentRequest sessionCommentRequest);

    List<SessionResponseInfo> checkSessionByDate(LocalDate date);

    void changeStatus(Long id, SessionStatus status);

    void delete(Long id);
}


