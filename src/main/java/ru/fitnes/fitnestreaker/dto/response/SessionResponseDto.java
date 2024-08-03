package ru.fitnes.fitnestreaker.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sessions")
public class SessionResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("trainingDateAndTime")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime trainingDateAndTime;

    @JsonProperty("status")
    private String status; // "scheduled", "completed", etc.

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("trainerId")
    private Long trainerId;
}
