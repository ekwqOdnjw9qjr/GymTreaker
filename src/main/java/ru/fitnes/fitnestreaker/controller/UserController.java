package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User",description = "Operation on users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final BaseResponseService baseResponseService;


    @Operation(
            summary = "Getting a user by ID",
            description = "Allows you to upload a user by ID from the database"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseWrapper<UserResponseDto> getUserById(@PathVariable @Min(1) Long id) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getById(id));
    }

    @Operation(
            summary = "Getting a info about you",
            description = "Allows you to check info about you"
    )
    @GetMapping("/me")
    public ResponseWrapper<UserResponseDto> getUserInfo() {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getUserInfo());
    }

    @Operation(
            summary = "User search by any available parameter",
            description = "Allows you to download all users from the database according to the specified data"
    )
    @GetMapping("/parameter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseWrapper<List<UserResponseDto>> getAllUsersByAnyAvailableParameter(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.searchUsersByAnyFields(email,firstName,lastName));
    }

    @Operation(
            summary = "Getting all the users",
            description = "Allows you to unload all users from the database"
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseWrapper<List<UserResponseDto>> getAllUsers() {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.getAll());
    }

    @Operation(
            summary = "Register new user in database",
            description = "Allows you to register a new user in the database"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<UserResponseDto> registerUser(@RequestBody UserRequestDto userDto) throws Exception{
        return baseResponseService.wrapSuccessResponse(userServiceImpl.registerNewUser(userDto));
    }

    @Operation(
            summary = "Change role by user ID",
            description = "Allows a user with admin role change role other users by user ID from the database"
    )
    @PatchMapping("/user/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseWrapper<UserResponseDto> changeUserRole(@PathVariable @Min(1) Long id, Role role) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.changeRole(id,role));
    }

    @Operation(
            summary = "Update user information",
            description = "Allows you to update user information in the database"
    )
    @PutMapping("/{id}")
    public ResponseWrapper<UserResponseDto> updateUser(@RequestBody  UserRequestDto userRequestDto,
                                                       @PathVariable @Min(1) Long id) {
        return baseResponseService.wrapSuccessResponse(userServiceImpl.update(userRequestDto,id));
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Allows you to delete a user by ID from the database"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Min(1) Long id,HttpSession session) {
        userServiceImpl.delete(id,session);
    }

    @Operation(
            summary = "Logout"
    )
    @PostMapping("/logout")
    public void logout(HttpSession httpSession) {
        userServiceImpl.logout(httpSession);
    }

}
