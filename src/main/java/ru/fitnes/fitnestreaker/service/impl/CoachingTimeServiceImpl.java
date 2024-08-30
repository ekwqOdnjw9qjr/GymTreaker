package ru.fitnes.fitnestreaker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.CoachingTimeService;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachingTimeServiceImpl implements CoachingTimeService {

    private final SecurityConfig securityConfig;
    private final TrainerRepository trainerRepository;
    private final CoachingTimeMapper coachingTimeMapper;
    private final CoachingTimeRepository coachingTimeRepository;

    @Override
    public CoachingTimeResponseDto findById(Long id) {
        CoachingTime coachingTime = coachingTimeRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        "Coaching time with id: " + id + " not found."));

        return coachingTimeMapper.coachingTimeResponseToDto(coachingTime);
    }

    @Override
    public CoachingTimeRequestDto create(CoachingTimeRequestDto coachingTimeRequestDto, DayOfWeek dayOfWeek) {

        CoachingTime coachingTime = coachingTimeMapper.coachingTimeRequestToEntity(coachingTimeRequestDto);

        Trainer trainer = trainerRepository.findTrainerByUserId(securityConfig.getCurrentUser().getId());

        coachingTime.setDayOfWeek(dayOfWeek);
        coachingTime.setTrainer(trainer);
        CoachingTime savedCoachingTime = coachingTimeRepository.save(coachingTime);

        return coachingTimeMapper.coachingTimeRequestToDto(savedCoachingTime);
    }

    @Override
    public void delete(Long id) {
        coachingTimeRepository.deleteById(id);
    }



}
