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
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.service.impl.MembershipServiceImpl;

import java.util.List;


@Validated
@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
@Tag(name = "Memberships",description = "Operation on memberships")
public class MembershipController {

    private final MembershipServiceImpl membershipServiceImpl;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a membership by ID",
            description = "Allows you to upload a membership by ID from the database"
    )
    @GetMapping("/membership/{id}")
    public ResponseWrapper<MembershipResponseDto> getMembershipById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.getById(id));
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
            summary = "Getting a membership by your user ID",
            description = "Allows you to upload a membership by you user ID from the database"
    )
    @GetMapping("/membership/yorId/{id}")
    public ResponseWrapper<List<MembershipResponseDto>> getMembershipByYourUserId(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.findMembershipByUserId(id));
    }

    @Operation(
            summary = "Getting current status of membership by membership ID",
            description = "Allows you to check status of your membership by by membership ID from the database"
    )
    @GetMapping("/check/{id}")
    public ResponseWrapper<MembershipStatus> checkMembershipStatus(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.checkStatus(id));
    }

    @Operation(
            summary = "Create a membership",
            description = "Allows you to create a new membership record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<MembershipRequestDto> createMembership(MembershipRequestDto membershipRequestDto,
                                                                  MembershipType membershipType) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.create(membershipRequestDto,
                membershipType));
    }

    @Operation(
            summary = "Freeze your membership by membership ID",
            description = "Adds a certain number of days specified in the membership type to your subscription end date"
    )
    @PatchMapping("/freeze/{id}")
    public ResponseWrapper<MembershipResponseDto> freezeMembership(@PathVariable  Long id, Long freezeDays) {
        return baseResponseService.wrapSuccessResponse(membershipServiceImpl.freezeMembership(id, freezeDays));
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
