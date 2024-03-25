package ru.app.exception;

import java.util.List;

public class InvalidRequestParameterException extends IllegalArgumentException {

    public InvalidRequestParameterException(String message) {
        super(message);
    }

    public InvalidRequestParameterException(String message, List<String> listParameters) {
        super(message + listParameters);
    }
}
