package com.client.helpers;

import com.client.exceptions.ClientServiceException;
import com.client.exceptions.ResourceNotFoundException;
import com.client.exceptions.ServiceUnavailableException;
import com.client.service.TokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private final TokenStorage tokenStorage;

    public RestClientUtil(RestTemplate restTemplate, TokenStorage tokenStorage) {
        this.restTemplate = restTemplate;
        this.tokenStorage = tokenStorage;
    }
    public <T> T getObjectWithHeaders(String url, HttpHeaders headers, Class<T> type) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, type);
        return response.getBody();
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

    // Helper method to create headers with token
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = tokenStorage.getToken();

        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    // For endpoints that don't require authentication (like /auth/register, /auth/login)
    public <T> T postForObject(String url, Object request, Class<T> responseType) {
        return restTemplate.postForObject(url, request, responseType);
    }

    // Convenience methods for common operations (with authentication)
    public <T> List<T> getList(String url, Class<T[]> responseType) {
        return executeRequest("GET List", () -> {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<T[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, responseType
            );
            return response.getBody() != null ? List.of(response.getBody()) : List.of();
        });
    }

    public <T> T getObject(String url, Class<T> responseType) {
        return executeRequest("GET Object", () -> {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, responseType
            );
            return response.getBody();
        });
    }

    public <T> T postObject(String url, Object request, Class<T> responseType) {
        return executeRequest("POST Object", () -> {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, responseType
            );
            return response.getBody();
        });
    }

    public void putObject(String url, Object request) {
        executeRequest("PUT Object", () -> {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);

            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            return null;
        });
    }

    public void deleteObject(String url) {
        executeRequest("DELETE Object", () -> {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            return null;
        });
    }
}