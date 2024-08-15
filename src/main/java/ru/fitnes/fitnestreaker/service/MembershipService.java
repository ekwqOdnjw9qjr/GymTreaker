package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;

import java.time.LocalDateTime;
import java.util.List;

public interface MembershipService {

     MembershipResponseDto getById(Long id);

     List<MembershipResponseDto> getAll();

     MembershipRequestDto create(MembershipRequestDto membershipRequestDto, MembershipType membershipType);

//     MembershipResponseDto freezeMembership(Long id, Long freezeDays);

     MembershipStatus checkStatus(Long id);

     void delete(Long id);

    LocalDateTime calculateEndDate(Membership membership);

     MembershipResponseDto freezeMembership(Long id,Long freezeDays);
}
