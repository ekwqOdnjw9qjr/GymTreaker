package ru.fitnes.fitnestreaker.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.MembershipStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;
import ru.fitnes.fitnestreaker.mapper.MembershipMapper;
import ru.fitnes.fitnestreaker.repository.MembershipRepository;
import ru.fitnes.fitnestreaker.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipService {

    private final MembershipMapper membershipMapper;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;


    public MembershipDto getById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Membership with id: " + id + " not found"));
        return membershipMapper.toDto(membership);
    }

    public List<MembershipDto> getAll() {
        List<Membership> membershipList = membershipRepository.findAll();
        return membershipMapper.toListDto(membershipList);
    }

    public MembershipDto create(MembershipDto membershipDto) {
        Membership membership = membershipMapper.toEntity(membershipDto);
        try {
            membership.setUser(userRepository.getReferenceById(membershipDto.getUserId()));
        } catch (EntityNotFoundException e) {
            throw new LocalException(ErrorType.NOT_FOUND,"Пользователь с ID " + membershipDto.getUserId() + " не найден.");
        }
        LocalDateTime membershipDateEnd = membership.getEndDate();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        if (localDateTimeNow.isAfter(membershipDateEnd)) {
            membership.setMembershipStatus(MembershipStatus.INACTIVE);
        } else {
            membership.setMembershipStatus(MembershipStatus.ACTIVE);
        }
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.toDto(savedMembership);
    }


    public MembershipDto freezeMembership(Long id, Long freezeDays) throws LocalException {
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
        return membershipMapper.toDto(savedMembership);
    }

    public MembershipDto checkStatus(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new LocalException(ErrorType.NOT_FOUND, "Membership with id: " + id + " not found"));
        LocalDateTime membershipEndDate = membership.getEndDate();
        LocalDateTime localDateTimeNoww = LocalDateTime.now();
        if (localDateTimeNoww.isAfter(membershipEndDate)) {
            membership.setMembershipStatus(MembershipStatus.INACTIVE);
        } else {
            membership.setMembershipStatus(MembershipStatus.ACTIVE);
        }
        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.toDto(savedMembership);
    }


    public MembershipDto update(MembershipDto membershipDto, Long id) {
        Membership oldMembership = membershipRepository.findById(id)
                .orElseThrow(()-> new LocalException(ErrorType.NOT_FOUND,"Membership with id: " + id + " not found"));
        Membership newMembership = membershipMapper.toEntity(membershipDto);
        membershipMapper.merge(oldMembership,newMembership);
        Membership savedMembership = membershipRepository.save(oldMembership);
        return membershipMapper.toDto(savedMembership);
    }

    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }
}
