package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Trainer")
public class TrainerRequestDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("specialty")
    private String specialty;

    @JsonProperty("description")
    private String description;

    @JsonProperty("userId")
    private Long userId;

//    @JsonProperty("coachingTimesIds")
//    private Set<Long> coachingTimesIds;

//    @JsonProperty("sessionsId")
//    private Set<Long> sessionsId;
}
