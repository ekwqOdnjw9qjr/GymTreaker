package ru.fitnes.fitnestreaker.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.service.impl.UserServiceImpl;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userServiceImpl;


    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        return "registration";
    }


    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("userRequestDto") UserRequestDto userRequestDto,
                               BindingResult bindingResult) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userServiceImpl.registerNewUser(userRequestDto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email","error.userRequestDto",e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
