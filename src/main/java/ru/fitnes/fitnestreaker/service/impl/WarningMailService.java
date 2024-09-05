package ru.fitnes.fitnestreaker.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;

import java.time.LocalDate;
import java.util.List;
/**
 * Сервис для отправки предупреждений пользователям о приближающемся окончании срока действия их членства.
 * Сервис проверяет, у каких пользователей истекает абонемент в ближайшие 10 дней,
 * и отправляет им уведомления.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarningMailService {


    private final MailService mailService;
    private final MembershipRepository membershipRepository;

    /**
     * Метод, запускаемый по расписанию для проверки абонементов, которые истекают через 10 дней.
     * Отправляет пользователям уведомления по электронной почте.
     *
     * @throws MessagingException если произошла ошибка при отправке письма.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkEndDate() throws MessagingException {
        log.info("Метод checkEndDate запущен");

        LocalDate now = LocalDate.now();
        LocalDate tenDaysFromNow = now.plusDays(10);

        List<Membership> expiringMemberships = membershipRepository.findAll()
                .stream()
                .filter(membership -> {
                    LocalDate endDate = membership.getEndDate();
                    return endDate != null && endDate.isBefore(tenDaysFromNow) && endDate.isAfter(now);
                })
                .toList();

        if (expiringMemberships.isEmpty()) {
            return;
        }

        for (Membership membership : expiringMemberships) {
            User user = membership.getUser();
            LocalDate endDate = membership.getEndDate();
            log.info("Отправка email пользователю: " + user.getEmail());
            mailService.sendMailIfMembershipClose(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    endDate
            );
        }
    }
}



