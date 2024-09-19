package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления тренировочными сессиями.
 * Этот сервис предоставляет методы для создания, получения, обновления и удаления сессий.
 * Также включает функционал для добавления комментариев тренера, изменения статуса сессии
 * и поиска доступных для записи слотов.
 */
public interface SessionService {

    /**
     * Получение информацию о сессии по её идентификатору.
     *
     * @param id идентификатор сессии.
     * @return информация о сессии.
     * @throws LocalException если сессия с указанным идентификатором не найдена.
     */
    SessionResponseDto getById(Long id);

    /**
     * Получение списка сессий текущего аутентифицированного пользователя.
     *
     * @return список с информацией о сессиях текущего пользователя.
     */
    List<SessionResponseDto> getYourSessions();

    /**
     * Получение списка всех сессий в базе данных.
     *
     * @return список с информацией обо всех сессиях.
     */
    List<SessionResponseDto> getAll();

    /**
     * Получение доступных для записи слотов, для тренировки в заданную пользователем дату.
     *
     * @param date дата для поиска доступных слотов.
     * @param id идентификатор тренера, для которого ищутся слоты.
     * @return список с доступными слотами для тренировки.
     */
    List<CoachingTimeResponseDto> getAvailableSlots(LocalDate date, Long id);

    /**
     * Создание новой сессии для аутентифицированного в данный момент пользователя.
     *
     * @param sessionRequestDto объект для создания новой сессии, содержащий необходимые для ввода данные.
     * @return информация о созданной сессии.
     * @throws IllegalArgumentException если у пользователя нет активного абонемента,
     *                                   если пользователь не связан с тренером,
     *                                   если дата и время сессии конфликтуют с уже существующими сессиями.
     */
    SessionResponseDto create(SessionRequestDto sessionRequestDto);

    /**
     * Добавление комментария тренера к сессии.
     *
     * @param id идентификатор сессии, к которой будет добавлен комментарий.
     * @param sessionCommentRequest объект, содержащий комментарий тренера.
     * @return информация о сессии с добавленным комментарием тренера.
     * @throws LocalException если сессия с указанным идентификатором не найдена,
     *                        текущий пользователь не имеет доступа для изменения комментария.
     */
    SessionCommentRequest addTrainerCommentForSessions(Long id,SessionCommentRequest sessionCommentRequest);

    /**
     * Изменение статуса сессии.
     *
     * @param id идентификатор сессии.
     * @param status новый статус сессии.
     * @throws LocalException если сессия с указанным идентификатором не найдена,
     *                        текущий пользователь не имеет доступа для изменения статуса,
     *                        статус сессии уже завершен или отменен.
     */
    void changeStatus(Long id, SessionStatus status);

    /**
     * Удаление сессию по её идентификатору.
     *
     * @param id идентификатор сессии.
     */
    void delete(Long id);
}


