package ru.fitnes.fitnestreaker.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Сервис для отправки электронных писем.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${MAIL_USERNAME}")
    private String EMAIL;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    /**
     * Отправление электронного письма пользователю после регистрации.
     *
     * @param to Адрес электронной почты получателя.
     * @param firstName Имя получателя.
     * @param lastName  Фамилия получателя.
     * @throws MessagingException если произошла ошибка при создании или отправке письма.
     */
    public void sendRegistrationMail(String to, String firstName, String lastName) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("username",firstName + " " + lastName);

        String process = templateEngine.process("registration_mail.html",context);

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setFrom(new InternetAddress(EMAIL));
        helper.setSubject("ZXC");
        helper.setText(process,true);

        mailSender.send(mimeMessage);
    }

    /**
     * Отправление электронного письма пользователю с предупреждением о скором окончании абонемента.
     *
     * @param to Адрес электронной почты получателя.
     * @param firstName Имя получателя.
     * @param lastName  Фамилия получателя.
     * @param endDate   Дата окончания абонемента.
     * @throws MessagingException если произошла ошибка при создании или отправке письма.
     */
    public void sendMailIfMembershipClose(String to,
                                          String firstName,
                                          String lastName,
                                          LocalDate endDate) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("username",firstName + " " + lastName);
        context.setVariable("endDate",endDate);

        String process = templateEngine.process("warning_mail.html",context);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setFrom(new InternetAddress(EMAIL));
        helper.setSubject("ZXC");
        helper.setText(process,true);

        mailSender.send(mimeMessage);
    }
}