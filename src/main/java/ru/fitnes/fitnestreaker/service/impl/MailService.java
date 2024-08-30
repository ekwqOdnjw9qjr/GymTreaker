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

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${MAIL_USERNAME}")
    private String EMAIL;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void SendRegistrationMail(String to, String firstName, String lastName) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("username",firstName + " " + lastName);

        String process = templateEngine.process("registration_mail.html",context);

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setFrom(new InternetAddress(EMAIL + "@yandex.ru"));
        helper.setSubject("ZXC");
        helper.setText(process,true);

        mailSender.send(mimeMessage);
    }

    public void SendMailIfMembershipClose(String to,
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
        helper.setFrom(new InternetAddress(EMAIL + "@yandex.ru"));
        helper.setSubject("ZXC");
        helper.setText(process,true);

        mailSender.send(mimeMessage);
    }
}