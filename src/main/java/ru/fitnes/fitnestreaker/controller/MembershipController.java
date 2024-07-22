package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.service.MembershipService;

import java.util.List;

@Tag(name = "Memberships",description = "Operation on memberships")
@Validated
@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    private final BaseResponseService baseResponseService;


    @Operation(
            summary = "Getting a membership by ID",
            description = "Allows you to upload a company by ID from the database"
    )
    @GetMapping("/membership/{id}")
    public ResponseWrapper<MembershipDto> getMembershipById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(membershipService.getById(id));
    }

    @Operation(
            summary = "Getting all the memberships",
            description = "Allows you to unload all memberships from the database"
    )
    @GetMapping
    public ResponseWrapper<List<MembershipDto>> getAllMembership() {
        return baseResponseService.wrapSuccessResponse(membershipService.getAll());
    }

    @Operation(
            summary = "Create a membership",
            description = "Allows you to create a new membership record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<MembershipDto> createMembership(MembershipDto membershipDto) {
        return baseResponseService.wrapSuccessResponse(membershipService.create(membershipDto));
    }

    @Operation(
            summary = "Update membership information",
            description = "Allows you to update membership information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<MembershipDto> updateMembership(@RequestBody @Valid MembershipDto membershipDto,@PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(membershipService.update(membershipDto,id));
    }

    @Operation(
            summary = "Delete a membership by ID",
            description = "Allows you to delete a membership by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteMembership(@PathVariable @Min(0) Long id) {
        membershipService.delete(id);
    }













}
