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
@Schema(description = "Users")
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

//    @JsonProperty("memberships")
//    private Set<Long> membershipsId;
//
//    @JsonProperty("sessionsId")
//    private Set<Long> sessionsId;
}
