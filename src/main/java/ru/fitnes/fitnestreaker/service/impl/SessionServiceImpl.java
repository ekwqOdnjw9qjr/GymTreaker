package ru.fitnes.fitnestreaker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.config.CustomUserDetails;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.SessionMapper;
import ru.fitnes.fitnestreaker.repository.*;
import ru.fitnes.fitnestreaker.service.SessionService;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class  SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final TrainerRepository trainerRepository;
    private final CoachingTimeRepository coachingTimeRepository;
    private final CoachingTimeServiceImpl coachingTimeService;



    @Override
    public SessionResponseDto getById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        return sessionMapper.sessionResponseToDto(session);
    }

    @Override
    public List<SessionResponseDto> getYourSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Session> session = sessionRepository.findSessionByUserId(customUserDetails.getId());
        return  sessionMapper.sessionResponseToListDto(session);
    }

    @Override
    public List<SessionResponseDto> getAll() {
        List<Session> sessionList = sessionRepository.findAll();
        return sessionMapper.sessionResponseToListDto(sessionList);
    }


    @Override
    public SessionRequestDto create(SessionRequestDto sessionRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        CoachingTimeResponseDto coachingTime = coachingTimeService.findById(sessionRequestDto.getCoachingTimeId());
        DayOfWeek dayOfTraining = sessionRequestDto.getDateOfTraining().getDayOfWeek();

        if (!coachingTime.getDayOfWeek().equals(dayOfTraining)) {
            throw new IllegalArgumentException("День недели в CoachingTime не совпадает с днем недели в дате тренировки.");
        }

        List<Session> conflictingSessions = sessionRepository.findConflictingSessions(
                sessionRequestDto.getTrainerId(),
                sessionRequestDto.getCoachingTimeId(),
                sessionRequestDto.getDateOfTraining(),
                coachingTime.getDayOfWeek());

        if (!conflictingSessions.isEmpty()) {
            throw new IllegalArgumentException("Session already exists for this trainer, time, and date.");
        }

        Session session = sessionMapper.sessionRequestToEntity(sessionRequestDto);
        session.setTrainer(trainerRepository.getReferenceById(sessionRequestDto.getTrainerId()));
        session.setUser(userRepository.getReferenceById(customUserDetails.getId())); // Установка пользователя
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
        newSession.setTrainer(trainerRepository.getReferenceById(sessionRequestDto.getTrainerId()));
//        newSession.setUser(userRepository.getReferenceById(sessionRequestDto.getUserId()));
        newSession.setCoachingTime(coachingTimeRepository.getReferenceById(sessionRequestDto.getCoachingTimeId()));
        Session savedSession = sessionRepository.save(oldSession);
        return sessionMapper.sessionRequestToDto(savedSession);
    }

    @Override
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }


}
