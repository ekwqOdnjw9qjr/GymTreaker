package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;



import java.time.DayOfWeek;



public interface CoachingTimeService {

     CoachingTimeResponseDto findById(Long id);
     CoachingTimeRequestDto create(CoachingTimeRequestDto coachingTimeRequestDto, DayOfWeek dayOfWeek);

     void delete(Long id);






}
