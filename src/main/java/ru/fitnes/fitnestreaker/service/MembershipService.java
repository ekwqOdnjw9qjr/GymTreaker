package ru.fitnes.fitnestreaker.service;


import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.enums.MembershipStatus;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;

import java.util.List;

public interface MembershipService {

     MembershipResponseDto getById(Long id);

    List<MembershipResponseDto> findYourMemberships();

    List<MembershipResponseDto> getAll();

     MembershipResponseDto create(MembershipRequestDto membershipRequestDto, MembershipType membershipType);

     MembershipResponseDto activeMembership(Long id, MembershipRequestDto membershipRequestDto);
     MembershipStatus checkStatus(Long id);

     void delete(Long id);

     MembershipResponseDto freezeMembership(Long id,Long freezeDays);
}
