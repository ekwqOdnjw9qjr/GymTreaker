package ru.fitnes.fitnestreaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fitnes.fitnestreaker.service.impl.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String text) {
        emailService.sendSimpleEmail(to, subject, text);
        return "Email sent successfully!";
    }
}
