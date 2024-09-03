package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.SessionMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.SessionRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.SessionService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class  SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final TrainerRepository trainerRepository;
    private final MembershipServiceImpl membershipService;
    private final MembershipRepository membershipRepository;
    private final CoachingTimeServiceImpl coachingTimeService;



    @Override
    public SessionResponseDto getById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));

        return sessionMapper.sessionResponseToDto(session);
    }

    @Override
    public List<SessionResponseDto> getYourSessions() {

        List<Session> session = sessionRepository.findSessionByUserId(securityConfig.getCurrentUser().getId());

        return sessionMapper.sessionResponseToListDto(session);
    }

    @Override
    public List<SessionResponseDto> getAll() {
        List<Session> sessionList = sessionRepository.findAll();

        return sessionMapper.sessionResponseToListDto(sessionList);
    }

    // нужно сделать сортировку по статусу

    @Override
    public SessionResponseDto create(SessionRequestDto sessionRequestDto) {

        List<Long> membershipsListId = membershipRepository.
                findMembershipsIdsByUserId(securityConfig.getCurrentUser().getId());

        boolean activeMembership = membershipsListId.stream()
                .anyMatch(id -> membershipService.checkStatus(id) == MembershipStatus.ACTIVE);

        if (!activeMembership) {
            throw new IllegalArgumentException("User dont have active membership.");
        }

        CoachingTimeResponseDto coachingTime = coachingTimeService.findById(sessionRequestDto.getCoachingTimeId());
        DayOfWeek dayOfTraining = sessionRequestDto.getDateOfTraining().getDayOfWeek();

        if (!coachingTime.getDayOfWeek().equals(dayOfTraining)) {
            throw new IllegalArgumentException("День недели в CoachingTime не совпадает с днем недели в дате тренировки.");
        }
        List<Session> conflictingSessions = sessionRepository.findConflictingSessions(
                sessionRequestDto.getCoachingTimeId(),
                sessionRequestDto.getDateOfTraining(),
                coachingTime.getDayOfWeek());

        if (!conflictingSessions.isEmpty()) {
            throw new IllegalArgumentException("Session already exists for this trainer, time, and date.");
        }
        Session session = sessionMapper.sessionRequestToEntity(sessionRequestDto);
        session.setStatus(SessionStatus.SCHEDULED);
        session.setUser(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()));
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.sessionResponseToDto(savedSession);
    }

    // переработаь систему статусов убрать метод и сделать колону в бд


    @Override
    public SessionCommentRequest addTrainerCommentForSessions(Long id,SessionCommentRequest sessionCommentRequest) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));

        if (!session.getCoachingTime().getTrainer().getUser().getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new LocalException(ErrorType.CLIENT_ERROR,
                    "You do not have access to change the status of this session.");
        }

        session.setTrainerComment(sessionCommentRequest.getTrainerComment());

        Session savedSession = sessionRepository.save(session);

        return sessionMapper.sessionCommentRequestToDto(savedSession);
    }

    @Override
    public List<SessionResponseInfo> checkSessionByDate(LocalDate date) {

        List<Session> sessionList = sessionRepository.findSessionsByDate(date);

        return sessionMapper.sessionResponseInfoToDto(sessionList);
    }

    @Override
    public void changeStatus(Long id, SessionStatus status) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));

        if (!session.getCoachingTime().getTrainer().getUser().getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new LocalException(ErrorType.CLIENT_ERROR,
                    "You do not have access to change the status of this session.");
        }

        if (session.getStatus() == SessionStatus.COMPLETED || session.getStatus() == SessionStatus.CANCELLED) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "Cannot change status of a completed session.");
        }
            session.setStatus(status);
            Session savedSession = sessionRepository.save(session);
            sessionMapper.sessionResponseToDto(savedSession);
    }

    @Override
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

}
