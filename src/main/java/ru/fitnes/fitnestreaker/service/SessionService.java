package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;

import java.util.List;

public interface SessionService {

    SessionResponseDto getById(Long id);

    List<SessionResponseDto> getYourSessions();

    List<SessionResponseDto> getAll();


    SessionRequestDto create(SessionRequestDto sessionRequestDto);

    Session registerSession(Long sessionId, Long userId);

    SessionRequestDto update(SessionRequestDto sessionRequestDto, Long id);

    void delete(Long id);
}


