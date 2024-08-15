package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.MembershipRequestDto;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;

import java.util.List;
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MembershipMapper {

    @Mapping(source = "user.id", target = "userId")
    MembershipRequestDto membershipRequestToDto(Membership membership);

    @Mapping(source = "userId", target = "user.id")
    Membership membershipRequestToEntity(MembershipRequestDto membershipRequestDto);

    @Mapping(source = "user.id", target = "userId")
    MembershipResponseDto membershipResponseToDto(Membership membership);
    @Mapping(source = "user.id", target = "userId")
    List<MembershipResponseDto> membershipResponseToListDto(List<Membership> membershipList);




//    List<Membership> toListEntity(List<MembershipRequestDto> membershipRequestDtoList);
    //    @Mapping(source = "userId", target = "user.id")
//    Membership membershipResponseToEntity(MembershipResponseDto membershipResponseDto);
//   List<MembershipRequestDto> membershipRequestToListDto(List<Membership> membershipList);


}
