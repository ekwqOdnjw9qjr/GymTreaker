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
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.MembershipStatus;
import ru.fitnes.fitnestreaker.service.impl.MembershipServiceImpl;

import java.util.List;

@Tag(name = "Memberships",description = "Operation on memberships")
@Validated
@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipServiceImpl membershipServiceImpl;
    private final BaseResponseService baseResponseService;


    @Operation(
            summary = "Getting a membership by ID",
            description = "Allows you to upload a company by ID from the database"
    )
    @GetMapping("/membership/{id}")
    public ResponseWrapper<MembershipResponseDto> getMembershipById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.getById(id));
    }



    @GetMapping("/freeze/{id}")
    public ResponseWrapper<MembershipResponseDto> freezeNew(@PathVariable  Long id, Long freezeDays) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.freezeMembership(id, freezeDays));
    }

    @Operation(
            summary = "Getting all the memberships",
            description = "Allows you to unload all memberships from the database"
    )
    @GetMapping
    public ResponseWrapper<List<MembershipResponseDto>> getAllMembership() {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.getAll());
    }

    @Operation(
            summary = "Create a membership",
            description = "Allows you to create a new membership record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<MembershipRequestDto> createMembership(MembershipRequestDto membershipRequestDto) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.create(membershipRequestDto));
    }

    @GetMapping("/check/{id}")
    public ResponseWrapper<MembershipStatus> checkStatus(@PathVariable @Min(0) Long id) {
         return baseResponseService.wrapSuccessResponse(membershipServiceImpl.checkStatus(id));
    }

    @Operation(
            summary = "Update membership information",
            description = "Allows you to update membership information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<MembershipRequestDto> updateMembership(@RequestBody @Valid MembershipRequestDto membershipRequestDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.update(membershipRequestDto,id));
    }

    @Operation(
            summary = "Delete a membership by ID",
            description = "Allows you to delete a membership by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteMembership(@PathVariable @Min(0) Long id) {
        membershipServiceImpl.delete(id);
    }













}
