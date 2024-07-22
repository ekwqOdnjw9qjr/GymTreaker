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
import ru.fitnes.fitnestreaker.dto.TrainerDto;
import ru.fitnes.fitnestreaker.service.TrainerService;

import java.util.List;

@Tag(name = "Trainers",description = "Operation on trainers")
@Validated
@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a trainer by ID",
            description = "Allows you to upload a trainer by ID from the database"
    )
    @GetMapping("/trainer/{id}")
    public ResponseWrapper<TrainerDto> getTrainerById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(trainerService.getById(id));
    }

    @Operation(
            summary = "Getting all the trainers",
            description = "Allows you to unload all trainers from the database"
    )
    @GetMapping
    public ResponseWrapper<List<TrainerDto>> getAllTrainer() {
        return baseResponseService.wrapSuccessResponse(trainerService.getAll());
    }

    @Operation(
            summary = "Create a trainer",
            description = "Allows you to create a new trainer record in the database"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<TrainerDto> createTrainer(@RequestBody @Valid TrainerDto trainerDto) {
        return baseResponseService.wrapSuccessResponse(trainerService.create(trainerDto));
    }

    @Operation(
            summary = "Update trainer information",
            description = "Allows you to update trainer information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<TrainerDto> updateTrainer(@RequestBody @Valid TrainerDto trainerDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(trainerService.update(trainerDto,id));
    }

    @Operation(
            summary = "Delete a trainer by ID",
            description = "Allows you to delete a trainer by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteTrainer(@PathVariable @Min(0) Long id) {
        trainerService.delete(id);
    }
}
