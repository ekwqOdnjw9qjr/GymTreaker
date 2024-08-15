package ru.fitnes.fitnestreaker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.config.SecurityConfig;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.mapper.UserMapper;
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
    private final SecurityConfig securityConfig;


    @Override
    public MembershipResponseDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        return membershipMapper.membershipResponseToDto(membership);
    }

    public List<MembershipResponseDto> findMembershipByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Trainer with id: " + id + " not found."));
        if (!user.getId().equals(securityConfig.getCurrentUser().getId())) {
            throw new RuntimeException("You do not have permission to check this user's data.");
        }
        List<Membership> membershipList = user.getMemberships();
        return membershipMapper.membershipResponseToListDto(membershipList);

    }

    @Override
    public List<MembershipResponseDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.membershipResponseToListDto(membershipList);
    }

    @Override
    public MembershipRequestDto create(MembershipRequestDto membershipRequestDto,MembershipType membershipType) {
        Membership membership = membershipMapper.membershipRequestToEntity(membershipRequestDto);
        membership.setUser(userRepository.getReferenceById(membershipRequestDto.getUserId()));
        membership.setMembershipType(membershipType);
        LocalDateTime endDate = calculateEndDate(membership);
        if (endDate == null) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "Вы выбрали недопустимое количество дней. " +
                    "Пожалуйста, выберите правильное количество дней.");
        }
        membership.setEndDate(endDate);
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.membershipRequestToDto(savedMembership);
    }



    public MembershipResponseDto freezeMembership(Long id, Long freezeDays) {
        if (freezeDays < 0) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "The number of freeze days cannot be negative");
        }
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        Long availableFreezeDays = membership.getFreezingDays();
        log.info("Available freeze days: " + availableFreezeDays);
        if (availableFreezeDays < freezeDays) {
            throw new LocalException(ErrorType.CLIENT_ERROR, "You don't have enough freeze days available");
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


    // Возможно стоит удалить или переработать мето update, т.к. я считаю что он должен обновлять тольоко владельца и
    // наверное длинну обаниента(продливание)

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
                return null; // Неизвестная продолжительность
            }
        }
        membership.setFreezingDays(membershipType.getFreezeDays());
        return membership.getStartDate().plusDays(membershipType.getDuration());
    }

}

