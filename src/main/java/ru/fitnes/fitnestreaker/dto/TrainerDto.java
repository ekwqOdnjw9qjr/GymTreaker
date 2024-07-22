package ru.fitnes.fitnestreaker.dto;

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
public class TrainerDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("specialty")
    private String specialty;

    @JsonProperty("sessionsId")
    private Set<Long> sessionsId;
}
