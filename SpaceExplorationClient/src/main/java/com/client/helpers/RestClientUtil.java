package com.client.helpers;

import com.client.exceptions.ClientServiceException;
import com.client.exceptions.ResourceNotFoundException;
import com.client.exceptions.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@Component
@Slf4j
public class RestClientUtil {

    private final RestTemplate restTemplate;

    public RestClientUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Generic method for all REST operations
    public <T> T executeRequest(String operation, Supplier<T> restCall) {
        log.info("Executing {} operation", operation);

        try {
            T result = restCall.get();
            log.info("Successfully completed {} operation", operation);
            return result;

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("{} - Resource not found: {}", operation, e.getMessage());
            throw new ResourceNotFoundException("Resource not found");

        } catch (HttpClientErrorException e) {
            log.error("{} - Client error: Status={}, Body={}",
                    operation, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ClientServiceException("Failed to process request");

        } catch (HttpServerErrorException e) {
            log.error("{} - Server error: Status={}", operation, e.getStatusCode());
            throw new ServiceUnavailableException("Server is experiencing issues. Please try again later.");

        } catch (ResourceAccessException e) {
            log.error("{} - Network error: {}", operation, e.getMessage());
            throw new ServiceUnavailableException("Unable to connect to service. Please try again later.");

        } catch (Exception e) {
            log.error("{} - Unexpected error: {}", operation, e.getMessage(), e);
            throw new ClientServiceException("An unexpected error occurred", e);
        }
    }

    // Convenience methods for common operations
    public <T> List<T> getList(String url, Class<T[]> responseType) {
        return executeRequest("GET List", () -> {
            ResponseEntity<T[]> response = restTemplate.getForEntity(url, responseType);
            return response.getBody() != null ? List.of(response.getBody()) : List.of();
        });
    }

    public <T> T getObject(String url, Class<T> responseType) {
        return executeRequest("GET Object", () ->
                restTemplate.getForObject(url, responseType));
    }

    public <T> T postObject(String url, Object request, Class<T> responseType) {
        return executeRequest("POST Object", () ->
                restTemplate.postForObject(url, request, responseType));
    }

    public void putObject(String url, Object request) {
        executeRequest("PUT Object", () -> {
            restTemplate.put(url, request);
            return null;
        });
    }

    public void deleteObject(String url) {
        executeRequest("DELETE Object", () -> {
            restTemplate.delete(url);
            return null;
        });
    }
}