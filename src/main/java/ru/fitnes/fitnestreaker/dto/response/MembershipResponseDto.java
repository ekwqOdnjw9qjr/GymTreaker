package ru.fitnes.fitnestreaker.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fitnes.fitnestreaker.entity.enums.MembershipType;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Memberships")
public class MembershipResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("startDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @JsonProperty("endDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @JsonProperty("membershipType")
    private MembershipType membershipType;

    @JsonProperty("freezingDays")
    private Long freezingDays;

    @JsonProperty("userId")
    private Long userId;
}
