package ru.fitnes.fitnestreaker.dto.response.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("dateOfTraining")
    private LocalDate dateOfTraining;

    @JsonProperty("coachingTimeInfo")
    private CoachingTimeResponse coachingTime;

    @JsonProperty("userInfo")
    private UserResponse user;

    @JsonProperty("comment")
    private String comment;

}
