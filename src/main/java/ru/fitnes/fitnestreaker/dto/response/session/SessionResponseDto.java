package ru.fitnes.fitnestreaker.dto.response.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sessions")
public class SessionResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("dateOfTraining")
    private LocalDate dateOfTraining;

    @JsonProperty("trainerInfo")
    private TrainerResponse trainer;

    @JsonProperty("coachingTimeInfo")
    private CoachingTimeResponse coachingTime;
}
