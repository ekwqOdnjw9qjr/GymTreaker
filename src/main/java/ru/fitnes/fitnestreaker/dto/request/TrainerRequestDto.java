package ru.fitnes.fitnestreaker.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, max = 25, message = "The first name must contain from 3 to 33 letters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]+$",
            message = "The first name can contain only lowercase and uppercase letters " +
                    "of the Russian and English alphabets ")
    @Schema(example = "Tom")
    private String firstName;

    @JsonProperty("last_name")
    @Size(min = 3, max = 33, message = "The last name must contain from 3 to 33 letters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]+$",
            message = "The last name can contain only lowercase and uppercase letters " +
                    "of the Russian and English alphabets ")
    @Schema(example = "Platz")
    private String lastName;

    @JsonProperty("specialty")
    @NotBlank
    @Schema(example = "Squats")
    private String specialty;

    @JsonProperty("description")
    @Size(max = 7777,message = "The max size of description must not exceed 7777 characters.")
    private String description;

}
