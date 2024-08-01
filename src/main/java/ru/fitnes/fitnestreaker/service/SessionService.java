package ru.fitnes.fitnestreaker.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.SessionDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.SessionMapper;
import ru.fitnes.fitnestreaker.repository.SessionRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    public SessionDto getById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        return sessionMapper.toDto(session);
    }

    public List<SessionDto> getAll() {
        List<Session> sessionList = sessionRepository.findAll();
        return sessionMapper.toListDto(sessionList);
    }

    public SessionDto create(SessionDto sessionDto) {
        Session session = sessionMapper.toEntity(sessionDto);
        session.setTrainer(trainerRepository.getReferenceById(sessionDto.getTrainerId()));
        session.setUser(userRepository.getReferenceById(sessionDto.getUserId()));
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.toDto(savedSession);
    }

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

    public SessionDto update(SessionDto sessionDto, Long id) {
        Session oldSession = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        Session newSession = sessionMapper.toEntity(sessionDto);
        sessionMapper.merge(oldSession,newSession);
        newSession.setTrainer(trainerRepository.getReferenceById(sessionDto.getTrainerId()));
        newSession.setUser(userRepository.getReferenceById(sessionDto.getUserId()));
        Session savedSession = sessionRepository.save(oldSession);
        return sessionMapper.toDto(savedSession);
    }

    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }


}
