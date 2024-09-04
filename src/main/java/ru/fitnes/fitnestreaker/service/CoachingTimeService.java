package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;

import java.time.DayOfWeek;
import java.util.List;


public interface CoachingTimeService {

     CoachingTimeResponseDto findById(Long id);
     CoachingTimeResponseDto create(CoachingTimeRequestDto coachingTimeRequestDto, DayOfWeek dayOfWeek);

     List<CoachingTimeResponseDto> findAll(Long id);
     void delete(Long id);

}
