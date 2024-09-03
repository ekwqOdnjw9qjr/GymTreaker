package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;
import ru.fitnes.fitnestreaker.security.SecurityConfig;
import ru.fitnes.fitnestreaker.service.MembershipService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final MembershipMapper membershipMapper;
    private final MembershipRepository membershipRepository;


    @Override
    public MembershipResponseDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        return membershipMapper.membershipResponseToDto(membership);
    }

    @Override
    public List<MembershipResponseDto> findYourMemberships() {


        List<Membership> membershipSet = membershipRepository
                .findMembershipsByUserId(securityConfig.getCurrentUser().getId());

        return membershipMapper.membershipResponseToListDto(membershipSet);
    }

    @Override
    public List<MembershipResponseDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.membershipResponseToListDto(membershipList);
    }

    @Override
    public MembershipResponseDto create(MembershipRequestDto membershipRequestDto,MembershipType membershipType) {


        Membership membership = membershipMapper.membershipRequestToEntity(membershipRequestDto);
        membership.setUser(userRepository.getReferenceById(securityConfig.getCurrentUser().getId()));
        membership.setMembershipType(membershipType);

        LocalDate endDate = calculateEndDate(membership);
        membership.setEndDate(endDate);

        Membership savedMembership = membershipRepository.save(membership);

        return membershipMapper.membershipResponseToDto(savedMembership);
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

        LocalDate updatedEndDate = membership.getEndDate().plusDays(freezeDays);
        membership.setEndDate(updatedEndDate);

        Long daysLeft = availableFreezeDays - freezeDays;
        membership.setFreezingDays(daysLeft);

        log.info("Days left after freezing: " + daysLeft);

        Membership savedMembership = membershipRepository.save(membership);

        return membershipMapper.membershipResponseToDto(savedMembership);
    }

    @Override
    public MembershipStatus checkStatus(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));

        if (!membership.getUser().getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new LocalException(ErrorType.CLIENT_ERROR,
                    "You do not have access to change the status of this session.");
        }

        LocalDate membershipEndDate = membership.getEndDate();

        LocalDate localDateNow = LocalDate.now();

        if (localDateNow.isAfter(membershipEndDate)) {
            return MembershipStatus.INACTIVE;
        } else {
            return MembershipStatus.ACTIVE;
        }
    }
    // добавить поле статус в сущность memberships и удалить этот метод
    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public LocalDate calculateEndDate(Membership membership) {
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