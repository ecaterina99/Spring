package com.client.service;

import com.client.DTO.UserDTO;
import com.client.helpers.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

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

    public <T> T get(String endpoint, Class<T> type) {
        return rest.getObject(apiUrl + endpoint, type);
    }

    public <T, R> R post(String endpoint, T body, Class<R> type) {
        return rest.postObject(apiUrl + endpoint, body, type);
    }
}

record LoginRequest(String email, String password) {
}

record TokenResponse(String accessToken) {
}

