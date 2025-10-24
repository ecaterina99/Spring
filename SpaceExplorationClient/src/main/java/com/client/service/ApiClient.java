package com.client.service;

import com.client.DTO.UserDTO;
import com.client.helpers.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
/**
 * Service responsible for interacting with the external API for authentication and user data.
 * Provides methods for user login, registration, and fetching the currently authenticated user.
 * Uses RestClientUtil for making REST calls and TokenStorage for managing the authentication token.
 * The API base URL is configurable via application properties.
 */
@Service
public class ApiClient {
    private final RestClientUtil rest;
    private final TokenStorage tokenStorage;

    public ApiClient(RestClientUtil rest, TokenStorage tokenStorage) {
        this.rest = rest;
        this.tokenStorage = tokenStorage;
    }

    @Value("${api.base-url:http://localhost:8080/api}")
    private String apiUrl;

    public UserDTO getCurrentUser() {
        String token = tokenStorage.getToken();
        if (token == null || token.isEmpty()) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return rest.getObjectWithHeaders(apiUrl + "/users/", headers, UserDTO.class);
    }

    public String login(String email, String password) {
        return rest.postObject(apiUrl + "/auth/login", new LoginRequest(email, password), TokenResponse.class).accessToken();
    }

    public void register(UserDTO user) {
        rest.postForObject(apiUrl + "/auth/register", user, Void.class);
    }
}

record LoginRequest(String email, String password) {
}

record TokenResponse(String accessToken) {
}

