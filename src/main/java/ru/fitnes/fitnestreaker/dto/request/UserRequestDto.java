package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Users")
public class UserRequestDto {

    @JsonProperty("firstName")
    @Size(min = 3, max = 25, message = "The first name must contain from 3 to 25 letters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]+$",
            message = "The first name can contain only lowercase and uppercase letters " +
                      "of the Russian and English alphabets ")
    private String firstName;

    @JsonProperty("lastName")
    @Size(min = 3, max = 33, message = "The last name must contain from 3 to 33 letters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]+$",
            message = "The last name can contain only lowercase and uppercase letters " +
                    "of the Russian and English alphabets ")
    private String lastName;

    @JsonProperty("email")
    @Email
    private String email;

    @JsonProperty("password")
    @Size(min = 5, max = 55555, message = "The password must contain from 5 to 5555 characters")
    private String password;

}
