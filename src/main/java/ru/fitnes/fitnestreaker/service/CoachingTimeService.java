package ru.fitnes.fitnestreaker.service;

import jakarta.persistence.EntityNotFoundException;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface CoachingTimeService {

     CoachingTimeRequestDto create(CoachingTimeRequestDto coachingTimeRequestDto);


    List<LocalDateTime> getTrainingSchedule();


}
