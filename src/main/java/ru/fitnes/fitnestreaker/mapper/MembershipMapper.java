package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.dto.TrainerDto;
import ru.fitnes.fitnestreaker.entity.Membership;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MembershipMapper {
    @Mapping(source = "user.id", target = "userId")
    MembershipDto toDto(Membership membership);
    @Mapping(source = "userId", target = "user.id")
    Membership toEntity(MembershipDto membershipDto);

    List<MembershipDto> toListDto(List<Membership> membershipList);

    List<Membership> toListEntity(List<MembershipDto> membershipDtoList);

    void merge(@MappingTarget Membership target, Membership source);
}
