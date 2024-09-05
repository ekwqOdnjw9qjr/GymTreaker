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

/**
 * Сервис для управления тренерами.
 * Этот сервис предоставляет методы для создания, получения, обновления и удаления тренеров.
 */
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

    /**
     * Получение информации о тренере по его идентификатору.
     *
     * @param id идентификатор тренера.
     * @return информация о тренере.
     * @throws LocalException если тренер с указанным идентификатором не найден.
     */
    @Override
    public TrainerResponseDto getById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));

        return trainerMapper.trainerResponseToDto(trainer);
    }

    /**
     * Получение списка всех тренеров.
     *
     * @return список с информацией обо всех тренерах.
     */
    @Override
    public List<TrainerResponseDto> getAll() {
        List<Trainer> trainerList = trainerRepository.findAll();

        return trainerMapper.trainerResponseToListDto(trainerList);

    }

    /**
     * Получение расписания тренера по его идентификатору.
     *
     * @param id идентификатор тренера.
     * @return список с информацией о расписании тренера.
     * @throws LocalException если тренер с указанным идентификатором не найден.
     */
    @Override
        public List<CoachingTimeResponseDto> findCoachingTimeByTrainerId(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Trainer with id: %d not found.", id)));

        List<CoachingTime> coachingTimeList = trainer.getCoachingTimes();

        return coachingTimeMapper.coachingTimeResponseToListDto(coachingTimeList);

    }

    /**
     * Создание информации о тренере.
     *
     * @param trainerRequestDto объект для создания нового тренера, содержащий необходимые для создания данные.
     * @return информация о созданном тренере.
     */
    @Override
    public TrainerResponseDto create(TrainerRequestDto trainerRequestDto) {

        Trainer trainer = trainerMapper.trainerRequestToEntity(trainerRequestDto);
        trainer.setCreatedBy(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()).getId());

        Trainer savedTrainer = trainerRepository.save(trainer);

        return trainerMapper.trainerResponseToDto(savedTrainer);
    }

    /**
     * Обновление информации о тренере по идентификатору.
     *
     * @param id идентификатор тренера.
     * @param trainerRequestDto объект, содержащий обновленные данные тренера.
     * @return информация об обновленном тренере.
     * @throws LocalException если тренер с указанным идентификатором не найден.
     */
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
    /**
     * Назначение указанного тренера в качестве основного для текущего пользователя.
     *
     * @param id идентификатор тренера, который будет назначен основным для текущего пользователя.
     * @throws LocalException если тренер или текущий пользователь не найдены.
     */
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

    /**
     * Удаление пользователя из списка студентов пользователя.
     *
     * @param id идентификатор пользователя, у которого удаляется тренер.
     * @throws LocalException если пользователь с указанным идентификатором не найден,
     *                        или текущий пользователь не имеет доступа для удаления тренера.
     */
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

    /**
     * Удаление текущего основного тренера у пользователя.
     *
     * @throws LocalException если текущий пользователь не найден.
     */
    @Override
    public void deleteTheMainTrainer() {
        User user = userRepository.findById(securityConfig.getCurrentUser().getId())
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("User with id: %d not found.", securityConfig.getCurrentUser().getId())));

        user.setTrainer(null);

        userRepository.save(user);
    }

    /**
     * Получение списка пользователей, которые занимаются у тренера.
     *
     * @param id идентификатор тренера.
     * @return список пользователей, которые занимаются с указанным тренером.
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public List<UserResponseDto> getUsersByTrainerId(Long id) {

        List<User> userList = trainerRepository.findUserByTrainerIdInUserTable(id);

        return userMapper.userResponseToListDto(userList);
    }
    /**
     * Удаление тренера по его идентификатору.
     *
     * @param id идентификатор тренера.
     */
    @PreAuthorize("#id == authentication.principal.id")
    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }
}
