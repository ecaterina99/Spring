package com.client.controllers;
import com.client.DTO.UserDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.client.service.ApiClient;
import com.client.service.TokenStorage;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ApiClient apiClient;
    private final TokenStorage tokenStorage;

    public GlobalControllerAdvice(ApiClient apiClient, TokenStorage tokenStorage) {
        this.apiClient = apiClient;
        this.tokenStorage = tokenStorage;
    }

    @ModelAttribute("currentUser")
    public UserDTO addUserToModel() {
        String token = tokenStorage.getToken();

        if (token != null && !token.isEmpty()) {
            try {
                return apiClient.getCurrentUser();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

 @ModelAttribute("accessToken")
    public String addTokenToModel() {
        String token = tokenStorage.getToken();
        return token != null ? token : "";
    }

}