package ru.fitnes.fitnestreaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.TrainerDto;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.Exception;
import ru.fitnes.fitnestreaker.mapper.TrainerMapper;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;

    public TrainerDto getById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new Exception(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        return trainerMapper.toDto(trainer);
    }

    public List<TrainerDto> getAll() {
        List<Trainer> trainerList = trainerRepository.findAll();
        return trainerMapper.toListDto(trainerList);
    }

    public TrainerDto create(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toDto(savedTrainer);
    }

    public TrainerDto update(TrainerDto trainerDto, Long id) {
        Trainer oldTrainer = trainerRepository.findById(id)
                .orElseThrow(()->  new Exception(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        Trainer newTrainer = trainerMapper.toEntity(trainerDto);
        trainerMapper.merge(oldTrainer, newTrainer);
        Trainer savedTrainer = trainerRepository.save(oldTrainer);
        return trainerMapper.toDto(savedTrainer);
    }

    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
