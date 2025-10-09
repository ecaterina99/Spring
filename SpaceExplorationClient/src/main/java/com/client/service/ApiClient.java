package com.client.service;

import com.client.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.base-url:http://localhost:8080/api}")
    private String apiUrl;

    public void register(UserDTO user) {
        restTemplate.postForEntity(apiUrl + "/auth/register", user, Void.class);
    }

    public String login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                apiUrl + "/auth/login", request, TokenResponse.class
        );
        return response.getBody().accessToken();
    }

    public <T> T get(String endpoint, String token, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiUrl + endpoint, HttpMethod.GET, entity, type
        ).getBody();
    }

    public <T, R> R post(String endpoint, T body, String token, Class<R> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<T> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                apiUrl + endpoint, HttpMethod.POST, entity, type
        ).getBody();
    }
}

record LoginRequest(String email, String password) {}
record TokenResponse(String accessToken) {}

