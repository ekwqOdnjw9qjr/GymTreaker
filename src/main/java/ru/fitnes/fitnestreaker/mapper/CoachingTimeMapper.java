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


    @Mapping(source = "trainers", target = "trainersIds", qualifiedByName = "trainersToIds")
    CoachingTimeRequestDto coachingTimeRequestToDto(CoachingTime coachingTime);


    CoachingTime coachingTimeRequestToEntity(CoachingTimeRequestDto coachingTimeRequestDto);

//    @Mapping(source = "trainers", target = "trainersIds", qualifiedByName = "trainersToIds")
    CoachingTimeResponseDto coachingTimeResponseToDto(CoachingTime coachingTime);
//    @Mapping(source = "trainers", target = "trainersIds", qualifiedByName = "trainersToIds")
    List<CoachingTimeResponseDto> coachingTimeResponseToListDto(List<CoachingTime> coachingTimeList);


    @Named("trainersToIds")
    default Set<Long> trainersToIds(Set<Trainer> trainers) {
        return trainers.stream()
                .map(Trainer::getId)
                .collect(Collectors.toSet());
    }



////    CoachingTime coachingTimeResponseToEntity(CoachingTimeResponseDto coachingTimeResponseDto);
//
//

//
//    List<CoachingTime> toListEntity(List<CoachingTimeRequestDto> coachingTimeRequestDtoList);
    //    List<CoachingTimeRequestDto> coachingTimeRequestToListDto(List<CoachingTime> coachingTimeList);


}
