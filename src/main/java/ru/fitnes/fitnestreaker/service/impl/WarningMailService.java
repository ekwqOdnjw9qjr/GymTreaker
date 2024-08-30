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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarningMailService {


    private final MailService mailService;
    private final MembershipRepository membershipRepository;

    @Scheduled(cron = "0 */5 * * * *")
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
            mailService.SendMailIfMembershipClose(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    endDate
            );
        }
    }
}



