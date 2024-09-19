package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Сервис для управления расписанием тренера.
 *
 * Предоставляет методы для создания, получения, удаления записей в расписании,
 * а также для получения всех записей по идентификатору тренера.
 */
public interface CoachingTimeService {

     /**
      * Получить запись в расписании по идентификатору.
      *
      * @param id идентификатор записи в расписании тренера.
      * @return запись в расписании тренера.
      * @throws LocalException если запись в расписании тренера с указанным идентификатором не найдена.
      */
     CoachingTimeResponseDto findById(Long id);

     /**
      * Создает новую запись в расписании тренера на указанный день недели.
      *
      * @param coachingTimeRequestDto объект для создания новой записи в расписания тренера.
      * @param dayOfWeek день недели, на который будет назначено запись в расписании тренера.
      * @return сохраненная запись в расписании тренера.
      */
     CoachingTimeResponseDto create(CoachingTimeRequestDto coachingTimeRequestDto, DayOfWeek dayOfWeek);

     /**
      * Получить все расписание тренера по идентификатору тренера.
      *
      * @param id идентификатор тренера.
      * @return все записи в расписании тренера.
      */
     List<CoachingTimeResponseDto> findAll(Long id);

     /**
      * Удалить запись в расписании тренера по идентификатору.
      *
      * @param id идентификатор записи в расписании тренера.
      */
     void delete(Long id);

}
