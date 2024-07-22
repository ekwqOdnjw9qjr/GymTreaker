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
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.service.TrainerService;
import ru.fitnes.fitnestreaker.service.UserService;

import java.util.List;

@Tag(name = "User",description = "Operation on users")
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a user by ID",
            description = "Allows you to upload a user by ID from the database"
    )
    @GetMapping("/user/{id}")
    public ResponseWrapper<UserDto> getUserById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(userService.getById(id));
    }

    @Operation(
            summary = "Getting all the users",
            description = "Allows you to unload all users from the database"
    )
    @GetMapping
    public ResponseWrapper<List<UserDto>> getAllUsers() {
        return baseResponseService.wrapSuccessResponse(userService.getAll());
    }

    @Operation(
            summary = "Create a user",
            description = "Allows you to create a new user record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<UserDto> createUser(UserDto userDto) {
        return baseResponseService.wrapSuccessResponse(userService.create(userDto));
    }

    @Operation(
            summary = "Update user information",
            description = "Allows you to update user information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<UserDto> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(userService.update(userDto,id));
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Allows you to delete a user by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable @Min(0) Long id) {
        userService.delete(id);
    }
}
