package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
@Schema(description = "Memberships")
public class MembershipRequestDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("startDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @JsonProperty("membershipDuration")
    private Long membershipDuration;


    @JsonProperty("userId")
    private Long userId;
}
