package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.TrainerMapper;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.service.TrainerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;

    @Override
    public TrainerResponseDto getById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        return trainerMapper.trainerResponseToDto(trainer);
    }

    @Override
    public List<TrainerResponseDto> getAll() {
        List<Trainer> trainerList = trainerRepository.findAll();
        return trainerMapper.trainerResponseToListDto(trainerList);

    }

    @Override
    public TrainerRequestDto create(TrainerRequestDto trainerRequestDto) {
        Trainer trainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.trainerRequestToDto(savedTrainer);
    }

    @Override
    public TrainerRequestDto update(TrainerRequestDto trainerRequestDto, Long id) {
        Trainer oldTrainer = trainerRepository.findById(id)
                .orElseThrow(()->  new LocalException(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        Trainer newTrainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
        trainerMapper.merge(oldTrainer, newTrainer);
        Trainer savedTrainer = trainerRepository.save(oldTrainer);
        return trainerMapper.trainerRequestToDto(savedTrainer);
    }

    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
