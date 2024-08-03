package ru.fitnes.fitnestreaker.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.util.List;

public interface SessionService {

    SessionResponseDto getById(Long id);

    List<SessionResponseDto> getAll();

    SessionRequestDto create(SessionRequestDto sessionRequestDto);

    Session registerSession(Long sessionId, Long userId);

    SessionRequestDto update(SessionRequestDto sessionRequestDto, Long id);

    void delete(Long id);
}


