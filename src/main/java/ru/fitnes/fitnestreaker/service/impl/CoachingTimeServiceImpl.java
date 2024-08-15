package ru.fitnes.fitnestreaker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.service.CoachingTimeService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachingTimeServiceImpl implements CoachingTimeService {

    private final CoachingTimeRepository coachingTimeRepository;
    private final CoachingTimeMapper coachingTimeMapper;
    private final TrainerRepository trainerRepository;


    @Override
    public CoachingTimeRequestDto create(CoachingTimeRequestDto coachingTimeRequestDto) {
        CoachingTime coachingTime = coachingTimeMapper.coachingTimeRequestToEntity(coachingTimeRequestDto);
        Set<Trainer> trainers = new HashSet<>();
        for (Long trainersIds : coachingTimeRequestDto.getTrainersIds()) {
            Trainer trainer = trainerRepository.getReferenceById(trainersIds);
            trainers.add(trainer);
        }
        coachingTime.setTrainers(trainers);
        CoachingTime savedCoachingTime = coachingTimeRepository.save(coachingTime);
        return coachingTimeMapper.coachingTimeRequestToDto(savedCoachingTime);
    }






    private Set<Trainer> getTrainersFromIds(Set<Long> trainerIds) {
        return trainerIds.stream()
                .map(id -> {
                    Trainer trainer = trainerRepository.getReferenceById(id);
                    if (trainer == null) {
                        throw new EntityNotFoundException("Trainer not found with ID: " + id);
                    }
                    return trainer;
                })
                .collect(Collectors.toSet());
    }
}
