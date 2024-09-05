package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.service.impl.CoachingTimeServiceImpl;

import java.time.DayOfWeek;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coaching-times")
@PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
@Tag(name = "Coaching Time", description = "Operation with CoachingTime")
public class CoachingTimeController {

    public final BaseResponseService baseResponseService;
    public final CoachingTimeServiceImpl coachingTimeServiceImpl;

    @Operation(
            summary = "Create a coaching time",
            description = "Allows you to create a new coaching time record in the database"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<CoachingTimeResponseDto> create(@RequestBody
                                                              CoachingTimeRequestDto coachingTimeRequestDto,
                                                              DayOfWeek dayOfWeek) {
        return baseResponseService.wrapSuccessResponse(coachingTimeServiceImpl
                .create(coachingTimeRequestDto,dayOfWeek));
    }
    @Operation(
            summary = "Get a coaching time by ID",
            description = "Allows get a coaching time by ID"
    )
    @GetMapping("/{id}")
    public ResponseWrapper<CoachingTimeResponseDto> getByID(@PathVariable @Min(1) Long id) {
        return baseResponseService.wrapSuccessResponse(coachingTimeServiceImpl.findById(id));
    }

    @Operation(
            summary = "Delete a coaching time by ID",
            description = "Allows you to delete a coaching time by ID in the database"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoachingTime(@PathVariable @Min(1) Long id) {
        coachingTimeServiceImpl.delete(id);
    }
}
