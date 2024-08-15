package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.mapper.TrainerMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.service.TrainerService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final CoachingTimeMapper coachingTimeMapper;

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

    public List<CoachingTimeResponseDto> findCoachingTimeByTrainerId(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        List<CoachingTime> coachingTimeSet = trainer.getCoachingTimes();
        return coachingTimeMapper.coachingTimeResponseToListDto(coachingTimeSet);

    }


    @Override
    public TrainerRequestDto create(TrainerRequestDto trainerRequestDto) {
        Trainer trainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
//        Set<CoachingTime> coachingTimes = new HashSet<>();
//        for (Long coachingTimeId : trainerRequestDto.getCoachingTimesIds()) {
//            CoachingTime coachingTime = coachingTimeRepository.getReferenceById(coachingTimeId);
//            coachingTimes.add(coachingTime);
//        }
//        trainer.setCoachingTimes(coachingTimes);
        trainer.setUser(userRepository.getReferenceById(trainerRequestDto.getUserId()));
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.trainerRequestToDto(savedTrainer);
    }

    @Override
    public TrainerRequestDto update(Long id, TrainerRequestDto trainerRequestDto) {
        Trainer oldTrainer = trainerRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Trainer with id: " + id + " not found."));
        trainerMapper.merge(oldTrainer,trainerMapper.trainerRequestToEntity(trainerRequestDto));
        Trainer savedTrainer = trainerRepository.save(oldTrainer);
        return trainerMapper.trainerRequestToDto(savedTrainer);
    }
    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
