package ru.fitnes.fitnestreaker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class MembershipDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("user")
    private Long userId;
}
