package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CoachingTimeMapper {


    CoachingTimeRequestDto coachingTimeRequestToDto(CoachingTime coachingTime);

    CoachingTime coachingTimeRequestToEntity(CoachingTimeRequestDto coachingTimeRequestDto);

    CoachingTimeResponseDto coachingTimeResponseToDto(CoachingTime coachingTime);

    List<CoachingTimeResponseDto> coachingTimeResponseToListDto(List<CoachingTime> coachingTimeList);

    Set<CoachingTimeResponseDto> coachingTimeResponseToSetDto(Set<CoachingTime> coachingTimeSet);

}







