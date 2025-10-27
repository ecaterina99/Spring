package com.server.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//Global scope: Catches exceptions from all controllers, not just one
/*
Handle exceptions in one place
Uniform error responses
Keep business logic separate from error handling
Meaningful error messages
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFound(EntityNotFoundException e) {
        return ApiError.of(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException e) {
        ApiError error = ApiError.of(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        return ApiError.builder()
                .error("VALIDATION_FAILED")
                .message("Invalid input data")
                .status(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(CrewOverflowException.class)
    public ResponseEntity<ApiError> handleCrewOverflow(CrewOverflowException e) {
        Map<String, Object> details = new HashMap<>();
        details.put("currentSize", e.getCurrentSize());
        details.put("maxSize", e.getMaxSize());

        ApiError error = ApiError.builder()
                .error("CREW_OVERFLOW")
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGenericException(Exception e) {
        return ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected error occurred");
    }
}