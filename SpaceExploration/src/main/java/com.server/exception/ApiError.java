package com.server.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;
    private Map<String, Object> details;
}