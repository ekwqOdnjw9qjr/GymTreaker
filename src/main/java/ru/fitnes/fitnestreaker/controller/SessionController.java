package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.SessionResponseDto;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.service.impl.SessionServiceImpl;

import java.util.List;

@Tag(name = "Sessions", description = "Operation on sessions")
@Validated
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionServiceImpl sessionServiceImpl;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a session by ID",
            description = "Allows you to upload a session by ID from the database"
    )
    @GetMapping("/session/{id}")
    public ResponseWrapper<SessionResponseDto> getSessionById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getById(id));
    }

    @Operation(
            summary = "Getting all the session",
            description = "Allows you to unload all sessions from the database"
    )
    @GetMapping
    public ResponseWrapper<List<SessionResponseDto>> getAllSession() {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getAll());
    }

    @Operation(summary = "Регистрация пациента.",
            description = "Метод принимает в себя id-талона и  id-пациента. Возвращает зарегистрированный талон к врачу.")
    @PatchMapping("/register/{ticketId}")
    public Session registerPatientTicket(@PathVariable Long sessionId, @RequestParam Long userId) {
        return sessionServiceImpl.registerSession(sessionId, userId);
    }

    @Operation(
            summary = "Create a session",
            description = "Allows you to create a new session record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(SessionRequestDto sessionRequestDto) {
        sessionServiceImpl.create(sessionRequestDto);
    }

    @Operation(
            summary = "Update session information",
            description = "Allows you to update session information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<SessionRequestDto> updateSession(@RequestBody @Valid SessionRequestDto sessionRequestDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.update(sessionRequestDto,id));
    }

    @Operation(
            summary = "Delete a session by ID",
            description = "Allows you to delete a session by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteSession(@PathVariable @Min(0) Long id) {
        sessionServiceImpl.delete(id);
    }
}
