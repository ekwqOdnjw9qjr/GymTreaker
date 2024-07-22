package ru.fitnes.fitnestreaker.exception;

import lombok.Getter;

@Getter
public class Exception extends RuntimeException {
    private final ErrorType type;

    public Exception(ErrorType type, String message) {
        super(message);
        this.type = type;
    }

    public Exception(ErrorType type, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
    }

    public Exception(ErrorType type, Throwable throwable) {
        super(throwable);
        this.type = type;
    }
}
