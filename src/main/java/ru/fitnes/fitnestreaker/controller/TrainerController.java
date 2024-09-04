package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.service.impl.TrainerServiceImpl;

import java.util.List;


@Validated
@RestController
@RequestMapping("api/v1/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers",description = "Operation on trainers")
public class TrainerController {

    private final TrainerServiceImpl trainerServiceImpl;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a trainer by ID",
            description = "Allows you to upload a trainer by ID from the database"
    )
    @GetMapping("/{id}")
    public ResponseWrapper<TrainerResponseDto> getTrainerById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.getById(id));
    }

    @Operation(
            summary = "Getting a trainer schedule by trainer ID",
            description = "Allows you to check a trainer schedule by trainer ID from the database"
    )
    @GetMapping("/trainer/{id}/schedule")
    public ResponseWrapper<List<CoachingTimeResponseDto>> getScheduleTrainerByTrainerId(@PathVariable @Min(0) Long id){
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.findCoachingTimeByTrainerId(id));
    }

    @Operation(
            summary = "Getting all the trainers",
            description = "Allows you to unload all trainers from the database"
    )
    @GetMapping
    public ResponseWrapper<List<TrainerResponseDto>> getAllTrainer() {
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.getAll());
    }

    @Operation(
            summary = "Create a trainer",
            description = "Allows you to create a new trainer record in the database"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public ResponseWrapper<TrainerResponseDto> createTrainer(@RequestBody @Valid TrainerRequestDto trainerRequestDto) {
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.create(trainerRequestDto));
    }

    @Operation(
            summary = "Update trainer information",
            description = "Allows you to update trainer information in the database"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public ResponseWrapper<TrainerResponseDto> updateTrainer(@PathVariable Long id, @RequestBody TrainerRequestDto trainerRequestDto) {
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.update(id,trainerRequestDto));
    }
    @Operation(
            summary = "Chose a main trainer",
            description = "Allows you to chose trainer who will you train with"
    )
    @PatchMapping("/main/{id}")
    public void setMainTrainer(@PathVariable Long id) {
        trainerServiceImpl.choosingTheMainTrainer(id);
    }

    @Operation(
            summary = "Delete a your trainer",
            description = "Allows you to delete trainer if you don't need a trainer"
    )
    @PatchMapping
    public void untieTheTrainer() {
        trainerServiceImpl.deleteTheMainTrainer();
    }

    @Operation(
            summary = "Kick a user in the of the students",
            description = "Allows you to kick a user in the of the students by ID"
    )
    @PatchMapping("trainer/{id}/user")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public void kickOutUserOfTheStudents(@PathVariable Long id) {
        trainerServiceImpl.kickOutUserOfTheStudents(id);
    }

    @Operation(
            summary = "Find all users who are engaged in this trainer",
            description = "Allows find all users who are engaged in this trainer by ID"
    )
    @GetMapping("trainer/{id}/users")
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public ResponseWrapper<List<UserResponseDto>> findAllUserByTrainer(@PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(trainerServiceImpl.getUsersByTrainerId(id));
    }

    @Operation(
            summary = "Delete a trainer by ID",
            description = "Allows you to delete a trainer by ID from the database"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_ADMIN')")
    public void deleteTrainer(@PathVariable @Min(0) Long id) {
        trainerServiceImpl.delete(id);
    }
}
