package com.pw.weatherapi.exceptions;

import java.util.List;

public class ApiError {
    private final List<String> errorMessages;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public ApiError(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
