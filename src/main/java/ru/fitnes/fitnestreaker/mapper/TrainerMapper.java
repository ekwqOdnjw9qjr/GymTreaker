package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.request.TrainerRequestDto;
import ru.fitnes.fitnestreaker.dto.response.TrainerResponseDto;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.List;
@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerRequestDto trainerRequestToDto(Trainer trainer);

    Trainer trainerRequestToEntity(TrainerRequestDto trainerRequestDto);

    TrainerResponseDto trainerResponseToDto(Trainer trainer);

    List<TrainerResponseDto> trainerResponseToListDto(List<Trainer> trainerList);

    void merge(@MappingTarget Trainer target, Trainer source);

    // List<Trainer> toListEntity(List<TrainerRequestDto> trainerRequestDtoList);
//    Trainer trainerResponseToEntity(TrainerResponseDto trainerResponseDto);
//    List<TrainerRequestDto> trainerRequestToListDto(List<Trainer> trainerList);
}
