package ru.fitnes.fitnestreaker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.service.impl.UserServiceImpl;
@Validated
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
    public String registerUser(@ModelAttribute @Valid UserRequestDto userDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userServiceImpl.registerNewUser(userDto);
        } catch (Exception e) {
            bindingResult.reject("registrationError", "Ошибка регистрации: " + e.getMessage());
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
