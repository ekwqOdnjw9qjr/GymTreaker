package ru.fitnes.fitnestreaker.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Coaching Time", description = "Operation with CoachingTime")
public class CoachingTimeRequestDto {


    @Schema(example = "07:10")
    @JsonProperty("startOfTraining")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startOfTraining;

    @Schema(example = "09:10")
    @JsonProperty("endOfTraining")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endOfTraining;

    @JsonProperty("trainersIds")
    private List<Long> trainersIds;

}
