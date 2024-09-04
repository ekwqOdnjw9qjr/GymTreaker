package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
public class SessionRequestDto {

    @JsonProperty("userComment")
    private String userComment;

    @NotNull
    @FutureOrPresent
    @JsonProperty("dateOfTraining")
    private LocalDate dateOfTraining;

    @NotNull
    @JsonProperty("coachingTimeId")
    private Long coachingTimeId;
}
