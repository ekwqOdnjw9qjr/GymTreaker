package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.mapper.TrainerMapper;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.TrainerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final UserMapper userMapper;
    private final TrainerMapper trainerMapper;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final CoachingTimeMapper coachingTimeMapper;

    @Override
    public TrainerResponseDto getById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));

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
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));

        List<CoachingTime> coachingTimeList = trainer.getCoachingTimes();

        return coachingTimeMapper.coachingTimeResponseToListDto(coachingTimeList);

    }

    @Override
    public TrainerResponseDto create(TrainerRequestDto trainerRequestDto) {

        Trainer trainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
        trainer.setCreatedBy(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()).getId());

        Trainer savedTrainer = trainerRepository.save(trainer);

        return trainerMapper.trainerResponseToDto(savedTrainer);
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public TrainerResponseDto update(Long id, TrainerRequestDto trainerRequestDto) {
        Trainer oldTrainer = trainerRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));

        trainerMapper.merge(oldTrainer,trainerMapper.trainerRequestToEntity(trainerRequestDto));

        Trainer savedTrainer = trainerRepository.save(oldTrainer);

        return trainerMapper.trainerResponseToDto(savedTrainer);
    }

    @Override
    public void choosingTheMainTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));


        User user = userRepository.findById(securityConfig.getCurrentUser().getId())
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", securityConfig.getCurrentUser().getId())));

        user.setTrainer(trainer);
        userRepository.save(user);
    }

    @Override
    public void kickOutUserOfTheStudents(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", id)));

        if (!user.getTrainer().getCreatedBy().equals(securityConfig.getCurrentUser().getId())) {
            throw new LocalException(ErrorType.CLIENT_ERROR,
                    "You do not have access to kick this user.");
        }

        user.setTrainer(null);

        userRepository.save(user);

    }

    @Override
    public void deleteTheMainTrainer() {
        User user = userRepository.findById(securityConfig.getCurrentUser().getId())
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", securityConfig.getCurrentUser().getId())));

        user.setTrainer(null);

        userRepository.save(user);
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public List<UserResponseDto> getUsersByTrainerId(Long id) {

        List<User> userList = trainerRepository.findUserByTrainerIdInUserTable(id);

        return userMapper.userResponseToListDto(userList);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
