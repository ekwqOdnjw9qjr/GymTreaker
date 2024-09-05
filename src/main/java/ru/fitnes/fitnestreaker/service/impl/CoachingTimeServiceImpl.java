package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.CoachingTimeService;

import java.time.DayOfWeek;
import java.util.List;
/**
 * Сервис для управления расписанием тренера.
 *
 * Предоставляет методы для создания, получения, удаления записей в расписании,
 * а также для получения всех записей по идентификатору тренера.
 */
@Service
@RequiredArgsConstructor
public class CoachingTimeServiceImpl implements CoachingTimeService {

    private final SecurityConfig securityConfig;
    private final TrainerRepository trainerRepository;
    private final CoachingTimeMapper coachingTimeMapper;
    private final CoachingTimeRepository coachingTimeRepository;


    /**
     * Получить запись в расписании по идентификатору.
     *
     * @param id идентификатор записи в расписании тренера.
     * @return запись в расписании тренера.
     * @throws LocalException если запись в расписании тренера с указанным идентификатором не найдена.
     */
    @Override
    public CoachingTimeResponseDto findById(Long id) {
        CoachingTime coachingTime = coachingTimeRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Coaching time with id: %d not found.", id)));

        return coachingTimeMapper.coachingTimeResponseToDto(coachingTime);
    }

    /**
     * Создает новую запись в расписании тренера на указанный день недели.
     *
     * @param coachingTimeRequestDto объект для создания новой записи в расписания тренера.
     * @param dayOfWeek день недели, на который будет назначено запись в расписании тренера.
     * @return сохраненная запись в расписании тренера.
     */
    @Override
    public CoachingTimeResponseDto create(CoachingTimeRequestDto coachingTimeRequestDto, DayOfWeek dayOfWeek) {

        CoachingTime coachingTime = coachingTimeMapper.coachingTimeRequestToEntity(coachingTimeRequestDto);

        Trainer trainer = trainerRepository.findTrainerByCreatedBy(securityConfig.getCurrentUser().getId());

        coachingTime.setDayOfWeek(dayOfWeek);
        coachingTime.setTrainer(trainer);
        CoachingTime savedCoachingTime = coachingTimeRepository.save(coachingTime);

        return coachingTimeMapper.coachingTimeResponseToDto(savedCoachingTime);
    }

    /**
     * Получить все расписание тренера по идентификатору тренера.
     *
     * @param id идентификатор тренера.
     * @return все записи в расписании тренера.
     */
    @Override
    public List<CoachingTimeResponseDto> findAll(Long id) {
        List<CoachingTime> coachingList = coachingTimeRepository.findAllCoachingTimeByTrainerId(id);
        return coachingTimeMapper.coachingTimeResponseToListDto(coachingList);
    }

    /**
     * Удалить запись в расписании тренера по идентификатору.
     *
     * @param id идентификатор записи в расписании тренера.
     */
    @Override
    public void delete(Long id) {
        coachingTimeRepository.deleteById(id);
    }



}
