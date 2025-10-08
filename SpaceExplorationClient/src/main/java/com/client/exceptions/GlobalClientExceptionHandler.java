package com.client.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalClientExceptionHandler {

    @ExceptionHandler(ApiProxyException.class)
    public ResponseEntity<String> handleApiProxyError(ApiProxyException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getResponseBody());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());

        model.addAttribute("errorTitle", "Resource Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404");
        model.addAttribute("suggestedAction", "Please check the URL or go back to the main page.");

        return "error/not-found";
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleServiceUnavailable(ServiceUnavailableException ex, Model model) {
        log.error("Service unavailable: {}", ex.getMessage());

        model.addAttribute("errorTitle", "Service Temporarily Unavailable");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "503");
        model.addAttribute("suggestedAction", "Please try again in a few moments or contact support if the problem persists.");

        return "error/service-unavailable";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Invalid request parameter: {}", ex.getMessage());

        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "400");
        model.addAttribute("suggestedAction", "Please check your input and try again.");

        return "error/bad-request";
    }

    @ExceptionHandler(ClientServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleClientServiceException(ClientServiceException ex, Model model) {
        log.error("Client service error: {}", ex.getMessage(), ex);

        model.addAttribute("errorTitle", "Application Error");
        model.addAttribute("errorMessage", "An error occurred while processing your request.");
        model.addAttribute("errorCode", "500");
        model.addAttribute("suggestedAction", "Please try again later or contact support if the problem persists.");

        return "error/general-error.html";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", "An unexpected error occurred while processing your request.");
        model.addAttribute("errorCode", "500");
        model.addAttribute("suggestedAction", "Please try again later or contact support.");

        return "error/general-error.html";
    }

}