package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;

import java.util.List;

public interface TrainerService {
     TrainerResponseDto getById(Long id);

     List<TrainerResponseDto> getAll();

     TrainerRequestDto create(TrainerRequestDto trainerRequestDto);

     TrainerRequestDto update(TrainerRequestDto trainerRequestDto, Long id);

     void delete(Long id);


}
