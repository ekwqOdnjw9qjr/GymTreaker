package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;

import java.util.List;
import java.util.Set;

public interface TrainerService {
     TrainerResponseDto getById(Long id);

     List<TrainerResponseDto> getAll();

     TrainerRequestDto create(TrainerRequestDto trainerRequestDto);

     TrainerRequestDto update( Long id,TrainerRequestDto trainerRequestDto);

     List<CoachingTimeResponseDto> findCoachingTimeByTrainerId(Long id);

     void delete(Long id);


}
