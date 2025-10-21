package com.client.controllers;
import com.client.DTO.BudgetDTO;
import com.client.DTO.UserDTO;
import com.client.service.BudgetService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.client.service.ApiClient;
import com.client.service.TokenStorage;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ApiClient apiClient;
    private final TokenStorage tokenStorage;
    private final BudgetService budgetService;

    public GlobalControllerAdvice(ApiClient apiClient, TokenStorage tokenStorage, BudgetService budgetService) {
        this.apiClient = apiClient;
        this.tokenStorage = tokenStorage;
        this.budgetService = budgetService;
    }


    @ModelAttribute("userBudget")
    public BudgetDTO getUserBudget() {
        String token = tokenStorage.getToken();

        if (token != null && !token.isEmpty()) {
            try {
                UserDTO currentUser = apiClient.getCurrentUser();
                if (currentUser != null) {
                    return budgetService.getUserBudget(currentUser.getId());
                }
            } catch (Exception e) {
                System.err.println("Error fetching user budget: " + e.getMessage());
                return null;
            }
        }
        return null;
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
    @ModelAttribute("userId")
    public Integer addUserIdToModel() {
        String token = tokenStorage.getToken();
        if (token != null && !token.isEmpty()) {
            try {
                UserDTO user = apiClient.getCurrentUser();
                return user != null ? user.getId() : null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}