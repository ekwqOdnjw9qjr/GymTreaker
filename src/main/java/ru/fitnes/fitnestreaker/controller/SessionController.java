package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.request.SessionRequestDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionCommentRequest;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseDto;
import ru.fitnes.fitnestreaker.dto.response.session.SessionResponseInfo;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;
import ru.fitnes.fitnestreaker.service.impl.SessionServiceImpl;

import java.time.LocalDate;
import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("api/v1/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Operation on sessions")
public class SessionController {

    private final SessionServiceImpl sessionServiceImpl;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a session by ID",
            description = "Allows you to upload a session by ID from the database"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public ResponseWrapper<SessionResponseDto> getSessionById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getById(id));
    }

    @Operation(
            summary = "Getting info about your sessions",
            description = "Allows you to upload info about your sessions"
    )
    @GetMapping("/my-sessions")
    public ResponseWrapper<List<SessionResponseDto>> getInfoAboutYourSessions() {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getYourSessions());
    }

    @Operation(
            summary = "Getting info about a sessions by trainer id",
            description = "A"
    )
    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseWrapper<List<SessionResponseInfo>> getInfoAboutSessionsForTrainer() {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getSessions());
    }

    @Operation(
            summary = "Getting all the session",
            description = "Allows you to unload all sessions from the database"
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public ResponseWrapper<List<SessionResponseDto>> getAllSession() {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.getAll());
    }

    @Operation(
            summary = "Trainer can add comment for session",
            description = "Allows trainer to add comment for session by ID"
    )
    @PatchMapping("/trainer-comment")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseWrapper<SessionCommentRequest> addTrainerCommentForSessions(Long id
            ,SessionCommentRequest sessionCommentRequest) {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.addTrainerCommentForSessions(id
                ,sessionCommentRequest));
    }

    @Operation(
            summary = "Create a session",
            description = "Allows you to create a new session record in the database"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(@RequestBody SessionRequestDto sessionRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User roles: {}", authentication.getAuthorities());
        sessionServiceImpl.create(sessionRequestDto);
    }


    @Operation(
            summary = "Change session status",
            description = "Allows trainer change session status by ID"
    )
    @PatchMapping("/session/{id}/status")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public void changeSessionStatus(@PathVariable @Min(0)Long id,SessionStatus status) {
        sessionServiceImpl.changeStatus(id,status);
    }
    @GetMapping("/date")
    public ResponseWrapper<List<SessionResponseInfo>> findByDate(LocalDate date) {
        return baseResponseService.wrapSuccessResponse(sessionServiceImpl.checkSessionByDate(date));
    }

    @Operation(
            summary = "Delete a session by ID",
            description = "Allows you to delete a session by ID from the database"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public void deleteSession(@PathVariable @Min(0) Long id) {
        sessionServiceImpl.delete(id);
    }
}
