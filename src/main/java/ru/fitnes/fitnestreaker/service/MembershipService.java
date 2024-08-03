package ru.fitnes.fitnestreaker.service;

import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.MembershipEndDate;
import ru.fitnes.fitnestreaker.entity.MembershipStatus;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.LocalException;

import java.time.LocalDateTime;
import java.util.List;

public interface MembershipService {

     MembershipResponseDto getById(Long id);

     List<MembershipResponseDto> getAll();

     MembershipRequestDto create(MembershipRequestDto membershipRequestDto);

     MembershipResponseDto freezeMembership(Long id, Long freezeDays);

     MembershipStatus checkStatus(Long id);

     MembershipRequestDto update(MembershipRequestDto membershipRequestDto, Long id);

     void delete(Long id);

    LocalDateTime calculateEndDate(Membership membership);

}
