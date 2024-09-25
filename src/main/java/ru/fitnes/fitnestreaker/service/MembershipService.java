package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.util.List;
/**
 * Сервис для управления абонементами.
 * Этот сервис предоставляет методы для создания, получения, обновления и удаления абонементов.
 * Также включает функционал для замораживания абонементов и проверки их статуса.
 */
public interface MembershipService {

    /**
     * Получить абонемент по идентификатору.
     *
     * @param id идентификатор абонемента.
     * @return информация об абонементе.
     * @throws LocalException если абонемент с указанным идентификатором не найден.
     */
     MembershipResponseDto getMembershipById(Long id);

    /**
     * Получение информации об абонементах аутентифицированного в данный момент пользователя.
     *
     * @return информация об абонементах аутентифицированного в данный момент пользователя.
     */
    List<MembershipResponseDto> findYourMemberships();

    /**
     * Получение информации о всех абонементах в базе данных.
     *
     * @return информация о всех абонементах в базе данных.
     */
    List<MembershipResponseDto> getAllMembership();

    /**
     * Создает новый абонемент для аутентифицированного в данный момент пользователя.
     * Если пользователь уже имеет активный абонемент, то новый абонемент создается без даты старта и окончания
     * абонемента, чтобы указать дату старта (активировать абонемент) пользователь может воспользоваться методом activeMembership.
     *
     * @param membershipRequestDto объект для создания нового абонемента.
     * @param membershipType тип абонемента.
     * @return сохраненный абонемент.
     */
     MembershipResponseDto createMembership(MembershipRequestDto membershipRequestDto, MembershipType membershipType);

    /**
     * Активация абонемента
     *
     * @param id идентификатор абонемента.
     * @param membershipRequestDto объект содержащий данные для активации абонемента.
     * @return сохраненный абонемент.
     */
     MembershipResponseDto activeMembership(Long id, MembershipRequestDto membershipRequestDto);

    /**
     * Проверяет текущий статус абонемента по его идентификатору.
     *
     * @param id идентификатор абонемента.
     * @return статус абонемента ACTIVE или INACTIVE.
     * @throws LocalException если:
     *     Абонемент с указанным идентификатором не найден,
     *     текущий пользователь не имеет доступа к данному абонементу.
     */
     MembershipStatus checkMembershipStatus(Long id);

    /**
     * Удалить абонемент по идентификатору.
     *
     * @param id идентификатор абонемента.
     */
     void deleteMembership(Long id);

     /**
     * Замораживает абонемент пользователя на указанное количество дней.
     *
     * @param id идентификатор абонемента.
     * @param freezeDays количество дней, на которое абонемент должен быть заморожен.
     * @return абонемент пользователя с обновленной информацией об абонементе после заморозки.
     * @throws LocalException если:
     *     Число дней заморозки отрицательное,
     *     абонемент с указанным идентификатором не найден,
     *     у пользователя недостаточно доступных дней для заморозки.
     */
     MembershipResponseDto freezeMembership(Long id,Long freezeDays);
}
