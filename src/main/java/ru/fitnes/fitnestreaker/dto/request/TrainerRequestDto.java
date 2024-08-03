package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Trainer")
public class TrainerRequestDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("specialty")
    private String specialty;

    @JsonProperty("coachingTimesIds")
    private Set<Long> coachingTimesIds;

//    @JsonProperty("sessionsId")
//    private Set<Long> sessionsId;
}
