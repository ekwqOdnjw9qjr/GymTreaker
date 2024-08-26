package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.SessionMapper;
import ru.fitnes.fitnestreaker.repository.*;
import ru.fitnes.fitnestreaker.service.SessionService;

import java.time.DayOfWeek;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class  SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
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
    public List<SessionResponseInfo> getSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Trainer trainer = trainerRepository.findByUserId(customUserDetails.getId());

        Specification<Session> spec = Specification.where(SessionSpecification.hasStatus(SessionStatus.SCHEDULED)
                .and(SessionSpecification.hasTrainer(trainer)));
        List<Session> sessionList = sessionRepository.findAll(spec);

        return sessionMapper.sessionResponseInfoToDto(sessionList);
    }

    // нужно сделать сортировку по статусу

    @Override
    public SessionRequestDto create(SessionRequestDto sessionRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<Long> membershipsListId = membershipRepository.findMembershipsListByUserId(customUserDetails.getId());

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
                sessionRequestDto.getTrainerId(),
                sessionRequestDto.getCoachingTimeId(),
                sessionRequestDto.getDateOfTraining(),
                coachingTime.getDayOfWeek());
        if (!conflictingSessions.isEmpty()) {
            throw new IllegalArgumentException("Session already exists for this trainer, time, and date.");
        }
        Session session = sessionMapper.sessionRequestToEntity(sessionRequestDto);
        session.setStatus(SessionStatus.SCHEDULED);
        session.setTrainer(trainerRepository.getReferenceById(sessionRequestDto.getTrainerId()));
        session.setUser(userRepository.getReferenceById(customUserDetails.getId())); // Установка пользователя
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.sessionRequestToDto(savedSession);
    }

    

    @Override
    public SessionCommentRequest addTrainerCommentForSessions(Long id,SessionCommentRequest sessionCommentRequest) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));
        session.setTrainerComment(sessionCommentRequest.getTrainerComment());
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.sessionCommentRequestToDto(savedSession);
    }

    @Override
    public void changeStatus(Long id, SessionStatus status) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Session with id: " + id + " not found."));

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
