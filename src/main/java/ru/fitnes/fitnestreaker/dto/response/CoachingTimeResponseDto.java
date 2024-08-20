package ru.fitnes.fitnestreaker.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Coaching Time", description = "Operation with CoachingTime")
public class CoachingTimeResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("startOfTraining")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startOfTraining;

    @JsonProperty("endOfTraining")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endOfTraining;

    @JsonProperty("dayOfWeek")
    private DayOfWeek dayOfWeek;

}
