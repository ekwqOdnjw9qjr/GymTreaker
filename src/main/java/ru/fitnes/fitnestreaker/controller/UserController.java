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
import ru.fitnes.fitnestreaker.dto.request.UserRequestDto;
import ru.fitnes.fitnestreaker.dto.response.UserResponseDto;
import ru.fitnes.fitnestreaker.service.impl.UserServiceImpl;

import java.util.List;

@Tag(name = "User",description = "Operation on users")
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a user by ID",
            description = "Allows you to upload a user by ID from the database"
    )
    @GetMapping("/user/{id}")
    public ResponseWrapper<UserResponseDto> getUserById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getById(id));
    }

    @Operation(
            summary = "User search by any available parameter",
            description = "Allows you to download all users from the database according to the specified data"
    )
    @GetMapping("/search")
    public ResponseWrapper<List<UserResponseDto>> getAllByCriteria(UserRequestDto userRequestDto) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.searchUsersByAnyFields(userRequestDto));
    }

    @Operation(
            summary = "Getting all the users",
            description = "Allows you to unload all users from the database"
    )
    @GetMapping
    public ResponseWrapper<List<UserResponseDto>> getAllUsers() {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getAll());
    }

    @Operation(
            summary = "Create a user",
            description = "Allows you to create a new user record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<UserRequestDto> createUser(UserRequestDto userRequestDto) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.create(userRequestDto));
    }

    @Operation(
            summary = "Update user information",
            description = "Allows you to update user information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<UserRequestDto> updateUser(@RequestBody @Valid UserRequestDto userRequestDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.update(userRequestDto,id));
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Allows you to delete a user by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable @Min(0) Long id) {
        userServiceImpl.delete(id);
    }
}
