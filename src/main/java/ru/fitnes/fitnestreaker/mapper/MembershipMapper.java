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

    @Mapping(source = "user.id", target = "userId")
    MembershipResponseDto membershipResponseToDto(Membership membership);
    @Mapping(source = "user.id", target = "userId")
    List<MembershipResponseDto> membershipResponseToListDto(List<Membership> membershipList);
    @Mapping(source = "user.id", target = "userId")
    Set<MembershipResponseDto> membershipResponseToSetDto(Set<Membership> membershipSet);


}
