package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.security.CustomUserDetails;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.service.MembershipService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipMapper membershipMapper;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;


    @Override
    public MembershipResponseDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        return membershipMapper.membershipResponseToDto(membership);
    }
    @Override
    public Set<MembershipResponseDto> findYourMemberships() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<Membership> membershipSet = membershipRepository.findMembershipsByUserId(customUserDetails.getId());
        return membershipMapper.membershipResponseToSetDto(membershipSet);

    }


    @Override
    public List<MembershipResponseDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.membershipResponseToListDto(membershipList);
    }

    @Override
    public MembershipRequestDto create(MembershipRequestDto membershipRequestDto,MembershipType membershipType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Membership membership = membershipMapper.membershipRequestToEntity(membershipRequestDto);
        membership.setUser(userRepository.getReferenceById(customUserDetails.getId()));
        membership.setMembershipType(membershipType);
        LocalDateTime endDate = calculateEndDate(membership);
        membership.setEndDate(endDate);
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.membershipRequestToDto(savedMembership);
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public MembershipResponseDto freezeMembership(Long id, Long freezeDays) {
        if (freezeDays < 0) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "The number of freeze days cannot be negative");
        }
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        Long availableFreezeDays = membership.getFreezingDays();
        log.info("Available freeze days: " + availableFreezeDays);
        if (availableFreezeDays < freezeDays) {
            throw new LocalException(ErrorType.CLIENT_ERROR,"You don't have enough freeze days available");
        }
        LocalDateTime updatedEndDate = membership.getEndDate().plusDays(freezeDays);
        Long daysLeft = availableFreezeDays - freezeDays;
        membership.setEndDate(updatedEndDate);
        membership.setFreezingDays(daysLeft);
        log.info("Days left after freezing: " + daysLeft);
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.membershipResponseToDto(savedMembership);
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public MembershipStatus checkStatus(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        LocalDateTime membershipEndDate = membership.getEndDate();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        if (localDateTimeNow.isAfter(membershipEndDate)) {
            return MembershipStatus.INACTIVE;
        } else {
            return MembershipStatus.ACTIVE;
        }
    }
    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public LocalDateTime calculateEndDate(Membership membership) {
        MembershipType membershipType = null;
        switch (membership.getMembershipType()) {
            case SMALL -> membershipType = MembershipType.SMALL;
            case BASIC -> membershipType = MembershipType.BASIC;
            case MEDIUM -> membershipType = MembershipType.MEDIUM;
            case LARGE -> membershipType = MembershipType.LARGE;
            case QUARTERLY -> membershipType = MembershipType.QUARTERLY;
            case ANNUAL -> membershipType = MembershipType.ANNUAL;
            default -> {
                return null;
            }
        }
        membership.setFreezingDays(membershipType.getFreezeDays());
        return membership.getStartDate().plusDays(membershipType.getDuration());
    }

}

