package com.rithika.clinicsystem.api;

import java.util.Map;

public class ApiException extends RuntimeException {

    private final int statusCode;
    private final Map<String, String> fieldErrors;

    public ApiException(String message) {

        super(message);

        this.statusCode = 0;
        this.fieldErrors = Map.of();
    }

    public ApiException(
            String message,
            int statusCode
    ) {

        super(message);

        this.statusCode = statusCode;
        this.fieldErrors = Map.of();
    }

    public ApiException(
            String message,
            int statusCode,
            Map<String, String> fieldErrors
    ) {

        super(message);

        this.statusCode = statusCode;

        this.fieldErrors =
                fieldErrors == null
                        ? Map.of()
                        : Map.copyOf(fieldErrors);
    }

    public ApiException(
            String message,
            Throwable cause
    ) {

        super(message, cause);

        this.statusCode = 0;
        this.fieldErrors = Map.of();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}