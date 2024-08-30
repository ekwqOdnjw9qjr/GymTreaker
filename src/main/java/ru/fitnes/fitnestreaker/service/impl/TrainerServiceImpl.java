package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.mapper.TrainerMapper;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.TrainerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
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

    @Override
        public List<CoachingTimeResponseDto> findCoachingTimeByTrainerId(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));

        List<CoachingTime> coachingTimeList = trainer.getCoachingTimes();

        return coachingTimeMapper.coachingTimeResponseToListDto(coachingTimeList);

    }

    // переделать на спецификацию поиск по имени или по фамилии

    @Override
    public TrainerRequestDto create(TrainerRequestDto trainerRequestDto) {

        Trainer trainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
        trainer.setUser(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()));

        Trainer savedTrainer = trainerRepository.save(trainer);

        return trainerMapper.trainerRequestToDto(savedTrainer);
    }
    // сделать метод чтобы тренер мог посмотреть кто на какой день к нему записан

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public TrainerRequestDto update(Long id, TrainerRequestDto trainerRequestDto) {
        Trainer oldTrainer = trainerRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Trainer with id: " + id + " not found."));

        trainerMapper.merge(oldTrainer,trainerMapper.trainerRequestToEntity(trainerRequestDto));

        Trainer savedTrainer = trainerRepository.save(oldTrainer);

        return trainerMapper.trainerRequestToDto(savedTrainer);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
