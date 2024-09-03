package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;

import java.time.LocalDate;
import java.util.List;

public interface MembershipService {

     MembershipResponseDto getById(Long id);

    List<MembershipResponseDto> findYourMemberships();

    List<MembershipResponseDto> getAll();

     MembershipResponseDto create(MembershipRequestDto membershipRequestDto, MembershipType membershipType);

     MembershipStatus checkStatus(Long id);

     void delete(Long id);

    LocalDate calculateEndDate(Membership membership);

     MembershipResponseDto freezeMembership(Long id,Long freezeDays);
}
