package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.CoachingTimeDto;
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Membership;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachingTimeMapper {


    CoachingTimeDto toDto(CoachingTime coachingTime);

    CoachingTime toEntity(CoachingTimeDto coachingTimeDto);

    List<CoachingTimeDto> toListDto(List<CoachingTime> coachingTimeList);

    List<CoachingTime> toListEntity(List<CoachingTimeDto> coachingTimeDtoList);

    void merge(@MappingTarget CoachingTime target, CoachingTime source);
}
