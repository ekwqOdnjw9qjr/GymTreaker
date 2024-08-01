package ru.fitnes.fitnestreaker.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.CoachingTimeDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.repository.CoachingTimeRepository;
import ru.fitnes.fitnestreaker.repository.TrainerRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachingTimeService {

    private final CoachingTimeRepository coachingTimeRepository;
    private final CoachingTimeMapper coachingTimeMapper;
    private final TrainerRepository trainerRepository;

    public CoachingTimeDto create(CoachingTimeDto coachingTimeDto) {
        // Преобразование DTO в сущность
        CoachingTime coachingTime = coachingTimeMapper.toEntity(coachingTimeDto);

        // Установка времени начала тренировок
        List<LocalDateTime> trainingTimes = getTrainingSchedule();
        coachingTime.setStartOfTraining(trainingTimes.get(0)); // Установка первого времени тренировки

        // Получение и установка тренеров
        Set<Trainer> trainers = getTrainersFromIds(coachingTimeDto.getTrainersIds());
        coachingTime.setTrainers(trainers);

        // Сохранение объекта CoachingTime в репозитории
        CoachingTime savedCoachingTime = coachingTimeRepository.save(coachingTime);

        // Преобразование сохраненного объекта обратно в DTO
        return coachingTimeMapper.toDto(savedCoachingTime);
    }

    // Метод для получения расписания тренировок
    private List<LocalDateTime> getTrainingSchedule() {
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
