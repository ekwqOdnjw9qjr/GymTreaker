package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.TrainerDto;
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.List;
@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDto toDto(Trainer trainer);

    Trainer toEntity(TrainerDto trainerDto);

    List<TrainerDto> toListDto(List<Trainer> trainerList);

    List<Trainer> toListEntity(List<TrainerDto> trainerDtoList);

    void merge(@MappingTarget Trainer target, Trainer source);
}
