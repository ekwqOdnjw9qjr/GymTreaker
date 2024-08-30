package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MembershipMapper {

    MembershipRequestDto membershipRequestToDto(Membership membership);

    Membership membershipRequestToEntity(MembershipRequestDto membershipRequestDto);


    MembershipResponseDto membershipResponseToDto(Membership membership);

    List<MembershipResponseDto> membershipResponseToListDto(List<Membership> membershipList);

    Set<MembershipResponseDto> membershipResponseToSetDto(Set<Membership> membershipSet);
}
