package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.service.impl.CoachingTimeServiceImpl;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Coaching Time", description = "Operation with CoachingTime")
@Validated
public class CoachingTimeController {

    public final BaseResponseService baseResponseService;
    public final CoachingTimeServiceImpl coachingTimeServiceImpl;

    @Operation(
            summary = "Create a payment",
            description = "Allows you to create a new payment record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<CoachingTimeRequestDto> create(@RequestBody CoachingTimeRequestDto coachingTimeRequestDto) {
        return baseResponseService.wrapSuccessResponse(coachingTimeServiceImpl.create(coachingTimeRequestDto));
    }
}
