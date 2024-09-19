package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.util.List;
/**
 * Сервис для управления тренерами.
 * Этот сервис предоставляет методы для создания, получения, обновления и удаления тренеров.
 */
public interface TrainerService {

     /**
      * Получение информации о тренере по его идентификатору.
      *
      * @param id идентификатор тренера.
      * @return информация о тренере.
      * @throws LocalException если тренер с указанным идентификатором не найден.
      */
     TrainerResponseDto getById(Long id);

     /**
      * Получение списка всех тренеров.
      *
      * @return список с информацией обо всех тренерах.
      */
     List<TrainerResponseDto> getAll();

     /**
      * Создание информации о тренере.
      *
      * @param trainerRequestDto объект для создания нового тренера, содержащий необходимые для создания данные.
      * @return информация о созданном тренере.
      */
     TrainerResponseDto create(TrainerRequestDto trainerRequestDto);

     /**
      * Обновление информации о тренере по идентификатору.
      *
      * @param id идентификатор тренера.
      * @param trainerRequestDto объект, содержащий обновленные данные тренера.
      * @return информация об обновленном тренере.
      * @throws LocalException если тренер с указанным идентификатором не найден.
      */
     TrainerResponseDto update( Long id,TrainerRequestDto trainerRequestDto);

     /**
      * Получение расписания тренера по его идентификатору.
      *
      * @param id идентификатор тренера.
      * @return список с информацией о расписании тренера.
      * @throws LocalException если тренер с указанным идентификатором не найден.
      */
     List<CoachingTimeResponseDto> findCoachingTimeByTrainerId(Long id);

     /**
      * Назначение указанного тренера в качестве основного для текущего пользователя.
      *
      * @param id идентификатор тренера, который будет назначен основным для текущего пользователя.
      * @throws LocalException если тренер или текущий пользователь не найдены.
      */
     void choosingTheMainTrainer(Long id);

     /**
      * Удаление пользователя из списка студентов пользователя.
      *
      * @param id идентификатор пользователя, у которого удаляется тренер.
      * @throws LocalException если пользователь с указанным идентификатором не найден,
      *                        или текущий пользователь не имеет доступа для удаления тренера.
      */
     void kickOutUserOfTheStudents(Long id);

     /**
      * Удаление текущего основного тренера у пользователя.
      *
      * @throws LocalException если текущий пользователь не найден.
      */
     void deleteTheMainTrainer();

     /**
      * Получение списка пользователей, которые занимаются у тренера.
      *
      * @param id идентификатор тренера.
      * @return список пользователей, которые занимаются с указанным тренером.
      */
     List<UserResponseDto> getUsersByTrainerId(Long id);

     /**
      * Удаление тренера по его идентификатору.
      *
      * @param id идентификатор тренера.
      */
     void delete(Long id);
}
