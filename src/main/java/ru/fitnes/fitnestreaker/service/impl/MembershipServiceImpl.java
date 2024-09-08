package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.MembershipService;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления абонементами.
 * Этот сервис предоставляет методы для создания, получения, обновления и удаления абонементов.
 * Также включает функционал для замораживания абонементов и проверки их статуса.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final UserServiceImpl userService;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final MembershipMapper membershipMapper;
    private final MembershipRepository membershipRepository;


    /**
     * Получить абонемент по идентификатору.
     *
     * @param id идентификатор абонемента.
     * @return информация об абонементе.
     * @throws LocalException если абонемент с указанным идентификатором не найден.
     */
    @Override
    public MembershipResponseDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Membership with id: %d not found.", id)));

        return membershipMapper.membershipResponseToDto(membership);
    }

    /**
     * Получение информации об абонементах аутентифицированного в данный момент пользователя.
     *
     * @return информация об абонементах аутентифицированного в данный момент пользователя.
     */
    @Override
    public List<MembershipResponseDto> findYourMemberships() {

            List<Membership> membershipList = membershipRepository
                .findMembershipsByUserId(securityConfig.getCurrentUser().getId());

        return membershipMapper.membershipResponseToListDto(membershipList);
    }

    /**
     * Получение информации о всех абонементах в базе данных.
     *
     * @return информация о всех абонементах в базе данных.
     */
    @Override
    public List<MembershipResponseDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.membershipResponseToListDto(membershipList);
    }

    /**
     * Создает новый абонемент для аутентифицированного в данный момент пользователя.
     * Если пользователь уже имеет активный абонемент, то новый абонемент создается без даты старта и окончания
     * абонемента, чтобы указать дату старта (активировать абонемент) пользователь может воспользоваться методом activeMembership.
     *
     * @param membershipRequestDto объект для создания нового абонемента.
     * @param membershipType тип абонемента.
     * @return сохраненный абонемент.
     */
    @Override
    public MembershipResponseDto create(MembershipRequestDto membershipRequestDto,MembershipType membershipType) {

        Membership membership = membershipMapper.membershipRequestToEntity(membershipRequestDto);

        membership.setUser(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()));
        membership.setMembershipType(membershipType);
        membership.setFreezingDays(membershipType.getFreezeDays());

        List<Membership> membershipList = membershipRepository
                .findMembershipsByUserId(securityConfig.getCurrentUser().getId());

        if (!membershipList.isEmpty()) {

            membership.setStartDate(null);
            membership.setEndDate(null);

        } else {

            if (membershipRequestDto.getStartDate() == null) {
                throw new IllegalArgumentException("Start date must not be null");
            }
            membership.setStartDate(membershipRequestDto.getStartDate());
            LocalDate endDate = calculateEndDate(membership);
            membership.setEndDate(endDate);
        }

        Membership savedMembership = membershipRepository.save(membership);


        return membershipMapper.membershipResponseToDto(savedMembership);
    }


    /**
     * Активация абонемента
     *
     * @param id идентификатор абонемента.
     * @param membershipRequestDto объект содержащий данные для активации абонемента.
     * @return сохраненный абонемент.
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public MembershipResponseDto activeMembership(Long id, MembershipRequestDto membershipRequestDto) {

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Membership with id: %d not found.", id)));

        if (membership.getStartDate() == null && membership.getEndDate() == null) {

            LocalDate startDate = membershipRequestDto.getStartDate();

            if (startDate == null) {
                throw new IllegalArgumentException("Start date must not be null");
            }

            membership.setStartDate(membershipRequestDto.getStartDate());
            LocalDate endDate = calculateEndDate(membership);
            membership.setEndDate(endDate);

        } else {
            throw new LocalException(ErrorType.CLIENT_ERROR, "Your membership is already active");
        }

        Membership savedMembership = membershipRepository.save(membership);

        return membershipMapper.membershipResponseToDto(savedMembership);
    }

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
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public MembershipResponseDto freezeMembership(Long id, Long freezeDays) {

        if (freezeDays < 0) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "The number of freeze days cannot be negative");
        }

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Membership with id: %d not found.", id)));

        Long availableFreezeDays = membership.getFreezingDays();

        log.info("Available freeze days: " + availableFreezeDays);
        if (availableFreezeDays < freezeDays) {
            throw new LocalException(ErrorType.CLIENT_ERROR,"You don't have enough freeze days available");
        }

        LocalDate updatedEndDate = membership.getEndDate().plusDays(freezeDays);
        membership.setEndDate(updatedEndDate);

        Long daysLeft = availableFreezeDays - freezeDays;
        membership.setFreezingDays(daysLeft);

        log.info("Days left after freezing: " + daysLeft);

        Membership savedMembership = membershipRepository.save(membership);

        return membershipMapper.membershipResponseToDto(savedMembership);
    }

    /**
     * Проверяет текущий статус абонемента по его идентификатору.
     *
     * @param id идентификатор абонемента.
     * @return статус абонемента ACTIVE или INACTIVE.
     * @throws LocalException если:
     *     Абонемент с указанным идентификатором не найден,
     *     текущий пользователь не имеет доступа к данному абонементу.
     */
    @Override
    public MembershipStatus checkStatus(Long id) {

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND,
                        String.format("Membership with id: %d not found.", id)));

        if (!membership.getUser().getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new LocalException(ErrorType.CLIENT_ERROR,
                    "You do not have access to check the status of this membership.");
        }

        LocalDate membershipEndDate = membership.getEndDate();
        LocalDate localDateNow = LocalDate.now();

        if (membershipEndDate == null || localDateNow.isAfter(membershipEndDate)) {
            return MembershipStatus.INACTIVE;
        } else {
            return MembershipStatus.ACTIVE;
        }
    }

    /**
     * Удалить абонемент по идентификатору.
     *
     * @param id идентификатор абонемента.
     */
    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }

    /**
     * Вычисляет дату окончания абонемента на основе типа абонемента и даты начала.
     *
     * @param membership объект, который содержит информацию об абонементе.
     * @return дата окончания абонемента.
     */
    private LocalDate calculateEndDate(Membership membership) {
        MembershipType membershipType = null;
        switch (membership.getMembershipType()) {
            case SMALL -> membershipType = MembershipType.SMALL;
            case BASIC -> membershipType = MembershipType.BASIC;
            case MEDIUM -> membershipType = MembershipType.MEDIUM;
            case LARGE -> membershipType = MembershipType.LARGE;
            case QUARTERLY -> membershipType = MembershipType.QUARTERLY;
            case ANNUAL -> membershipType = MembershipType.ANNUAL;
            default -> {
                return null;
            }
        }
        return membership.getStartDate().plusDays(membershipType.getDuration());
    }
}