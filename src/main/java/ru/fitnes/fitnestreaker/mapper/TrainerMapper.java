package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.entity.CoachingTime;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TrainerMapper {


    TrainerRequestDto trainerRequestToDto(Trainer trainer);

    Trainer trainerRequestToEntity(TrainerRequestDto trainerRequestDto);

    @Mapping(source = "user.id", target = "userId")
    TrainerResponseDto trainerResponseToDto(Trainer trainer);
    @Mapping(source = "user.id", target = "userId")
    List<TrainerResponseDto> trainerResponseToListDto(List<Trainer> trainerList);

     @Mapping(target = "id", ignore = true)
     void merge(@MappingTarget Trainer target,Trainer source);


}
