package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.CoachingTimeDto;
import ru.fitnes.fitnestreaker.mapper.CoachingTimeMapper;
import ru.fitnes.fitnestreaker.service.CoachingTimeService;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Coaching Time", description = "Operation with CoachingTime")
@Validated
public class CoachingTimeController {

    public final BaseResponseService baseResponseService;
    public final CoachingTimeService coachingTimeService;

    @Operation(
            summary = "Create a payment",
            description = "Allows you to create a new payment record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<CoachingTimeDto> create( @RequestBody CoachingTimeDto coachingTimeDto) {
        return baseResponseService.wrapSuccessResponse(coachingTimeService.create(coachingTimeDto));
    }
}
