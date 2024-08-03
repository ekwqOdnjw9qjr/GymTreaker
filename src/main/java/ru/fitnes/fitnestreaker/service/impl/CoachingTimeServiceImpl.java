package ru.fitnes.fitnestreaker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;
import ru.fitnes.fitnestreaker.service.CoachingTimeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachingTimeServiceImpl implements CoachingTimeService {

    private final CoachingTimeRepository coachingTimeRepository;
    private final CoachingTimeMapper coachingTimeMapper;
    private final TrainerRepository trainerRepository;

    @Override
    public CoachingTimeRequestDto create(CoachingTimeRequestDto coachingTimeRequestDto) {
        // Преобразование DTO в сущность
        CoachingTime coachingTime = coachingTimeMapper.coachingTimeRequestToEntity(coachingTimeRequestDto);

        // Установка времени начала тренировок
        List<LocalDateTime> trainingTimes = getTrainingSchedule();
        coachingTime.setStartOfTraining(trainingTimes.get(0)); // Установка первого времени тренировки

        // Получение и установка тренеров
        Set<Trainer> trainers = getTrainersFromIds(coachingTimeRequestDto.getTrainersIds());
        coachingTime.setTrainers(trainers);

        // Сохранение объекта CoachingTime в репозитории
        CoachingTime savedCoachingTime = coachingTimeRepository.save(coachingTime);

        // Преобразование сохраненного объекта обратно в DTO
        return coachingTimeMapper.coachingTimeRequestToDto(savedCoachingTime);
    }

    // Метод для получения расписания тренировок
    @Override
    public List<LocalDateTime> getTrainingSchedule() {
        LocalDateTime baseDate = LocalDateTime.of(2024, 7, 25, 8, 30);
        return List.of(
                baseDate,
                baseDate.plusHours(2),
                baseDate.plusHours(4),
                baseDate.plusHours(6),
                baseDate.plusHours(8)
        );
    }


    private Set<Trainer> getTrainersFromIds(Set<Long> trainerIds) {
        return trainerIds.stream()
                .map(id -> {
                    Trainer trainer = trainerRepository.getReferenceById(id);
                    if (trainer == null) {
                        // Логгирование или обработка ошибки, если тренер не найден
                        throw new EntityNotFoundException("Trainer not found with ID: " + id);
                    }
                    return trainer;
                })
                .collect(Collectors.toSet());
    }
}
