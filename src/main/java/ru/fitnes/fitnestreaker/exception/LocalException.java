package ru.fitnes.fitnestreaker.exception;

import lombok.Getter;

@Getter
public class LocalException extends RuntimeException {
    private final ErrorType type;

    public LocalException(ErrorType type, String message) {
        super(message);
        this.type = type;
    }

    public LocalException(ErrorType type, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
    }

    public LocalException(ErrorType type, Throwable throwable) {
        super(throwable);
        this.type = type;
    }
}
