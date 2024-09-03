package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import ru.fitnes.fitnestreaker.dto.request.CoachingTimeRequestDto;
import ru.fitnes.fitnestreaker.dto.response.CoachingTimeResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachingTimeMapper {


    CoachingTimeRequestDto coachingTimeRequestToDto(CoachingTime coachingTime);

    CoachingTime coachingTimeRequestToEntity(CoachingTimeRequestDto coachingTimeRequestDto);

    CoachingTimeResponseDto coachingTimeResponseToDto(CoachingTime coachingTime);

    List<CoachingTimeResponseDto> coachingTimeResponseToListDto(List<CoachingTime> coachingTimeList);



}







