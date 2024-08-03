package ru.fitnes.fitnestreaker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.SessionMapper;
import ru.fitnes.fitnestreaker.repository.SessionRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.service.SessionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Override
    public SessionResponseDto getById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        return sessionMapper.sessionResponseToDto(session);
    }

    @Override
    public List<SessionResponseDto> getAll() {
        List<Session> sessionList = sessionRepository.findAll();
        return sessionMapper.sessionResponseToListDto(sessionList);
    }

    @Override
    public SessionRequestDto create(SessionRequestDto sessionRequestDto) {
        Session session = sessionMapper.sessionRequestToEntity(sessionRequestDto);
        session.setTrainer(trainerRepository.getReferenceById(sessionRequestDto.getTrainerId()));
        session.setUser(userRepository.getReferenceById(sessionRequestDto.getUserId()));
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.sessionRequestToDto(savedSession);
    }

    @Override
    @Transactional
    public Session registerSession (Long sessionId, Long userId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,"Ticket not found with id: " + sessionId));

        if (session.getUser() != null) {
            throw new IllegalStateException("Session is already occupied");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + userId));

        session.setUser(user);
        return sessionRepository.save(session);
    }

    @Override
    public SessionRequestDto update(SessionRequestDto sessionRequestDto, Long id) {
        Session oldSession = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        Session newSession = sessionMapper.sessionRequestToEntity(sessionRequestDto);
        sessionMapper.merge(oldSession,newSession);
        newSession.setTrainer(trainerRepository.getReferenceById(sessionRequestDto.getTrainerId()));
        newSession.setUser(userRepository.getReferenceById(sessionRequestDto.getUserId()));
        Session savedSession = sessionRepository.save(oldSession);
        return sessionMapper.sessionRequestToDto(savedSession);
    }

    @Override
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }


}
