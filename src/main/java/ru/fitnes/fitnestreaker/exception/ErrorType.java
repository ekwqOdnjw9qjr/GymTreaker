package ru.fitnes.fitnestreaker.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    COMMON_ERROR("Business logic error", "Try to repeat the request later"),
    NOT_FOUND("The resource could not be found", "The resource was not found according to your request"),
    CLIENT_ERROR("Error in the request", "Check the parameters and repeat the request");

    private final String title;
    private final String text;
}
