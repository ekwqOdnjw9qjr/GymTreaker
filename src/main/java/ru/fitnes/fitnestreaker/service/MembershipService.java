package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MembershipService {

     MembershipResponseDto getById(Long id);


    Set<MembershipResponseDto> findMembershipByUserId(Long id);

    List<MembershipResponseDto> getAll();

     MembershipRequestDto create(MembershipRequestDto membershipRequestDto, MembershipType membershipType);

     MembershipStatus checkStatus(Long id);

     void delete(Long id);

    LocalDateTime calculateEndDate(Membership membership);

     MembershipResponseDto freezeMembership(Long id,Long freezeDays);
}
