package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.SessionDto;
import ru.fitnes.fitnestreaker.service.SessionService;

import java.util.List;

@Tag(name = "Sessions", description = "Operation on sessions")
@Validated
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a session by ID",
            description = "Allows you to upload a session by ID from the database"
    )
    @GetMapping("/session/{id}")
    public ResponseWrapper<SessionDto> getSessionById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(sessionService.getById(id));
    }

    @Operation(
            summary = "Getting all the session",
            description = "Allows you to unload all sessions from the database"
    )
    @GetMapping
    public ResponseWrapper<List<SessionDto>> getAllSession() {
        return baseResponseService.wrapSuccessResponse(sessionService.getAll());
    }

    @Operation(
            summary = "Create a session",
            description = "Allows you to create a new session record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(SessionDto sessionDto) {
        sessionService.create(sessionDto);
    }

    @Operation(
            summary = "Update session information",
            description = "Allows you to update session information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<SessionDto> updateSession(@RequestBody @Valid SessionDto sessionDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(sessionService.update(sessionDto,id));
    }

    @Operation(
            summary = "Delete a session by ID",
            description = "Allows you to delete a session by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteSession(@PathVariable @Min(0) Long id) {
        sessionService.delete(id);
    }
}
