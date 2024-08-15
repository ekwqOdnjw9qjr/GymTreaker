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
import ru.fitnes.fitnestreaker.entity.enums.Role;
import ru.fitnes.fitnestreaker.service.impl.UserServiceImpl;

import java.util.List;


@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User",description = "Operation on users")
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
            summary = "Getting a user info by your email",
            description = "Allows you to check info about you by you email"
    )
    @GetMapping("/user/info")
    public ResponseWrapper<UserResponseDto> getUserInfoByEmail(String email) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getUserInfoByEmail(email));
    }

    @Operation(
            summary = "User search by any available parameter",
            description = "Allows you to download all users from the database according to the specified data"
    )
    @GetMapping("/search")
    public ResponseWrapper<List<UserResponseDto>> getAllUsersByAnyAvailableParameter(UserRequestDto userRequestDto) {
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
            summary = "Register new user in database",
            description = "Allows you to register a new user in the database"
    )
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<UserRequestDto> registerUser(@RequestBody UserRequestDto userDto) throws Exception {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.registerNewUser(userDto));
    }

    @Operation(
            summary = "Change role by user ID",
            description = "Allows a user with admin role change role other users by user ID from the database"
    )
    @PatchMapping("/user/change/{id}")
    public ResponseWrapper<UserResponseDto> changeUserRole(@PathVariable @Min(0) Long id, Role role) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.changeRole(id,role));
    }

    @Operation(
            summary = "Update user information",
            description = "Allows you to update user information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<UserRequestDto> updateUser(@RequestBody  UserRequestDto userRequestDto, @PathVariable Long id) {
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
