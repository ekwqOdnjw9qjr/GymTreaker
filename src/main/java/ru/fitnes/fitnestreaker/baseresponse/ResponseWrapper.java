package ru.fitnes.fitnestreaker.baseresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "The wrapper for the answer")
public class ResponseWrapper<T> {
    @Schema(description = "Flag for successful execution of the request")
    private boolean success;

    @Schema(description = "Request Body")
    private T body;

    @Schema(description = "Error description")
    private ErrorDto error;
}