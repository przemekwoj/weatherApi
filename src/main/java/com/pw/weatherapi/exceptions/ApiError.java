package com.pw.weatherapi.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiError {
    private final List<String> errorMessages;

    public ApiError(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
