package com.pw.weatherapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errorMessages = new ArrayList<>();
        e.getConstraintViolations().forEach(constraint -> errorMessages.add(constraint.getMessage()));
        ApiError apiError = new ApiError(errorMessages);
        return ResponseEntity.badRequest()
                .body(apiError);
    }

    @ExceptionHandler(LocationNotFound.class)
    ResponseEntity<ApiError> handleLocationNotFound(LocationNotFound e) {
        ApiError apiError = new ApiError(Collections.singletonList(e.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }
}
