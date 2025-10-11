package com.client.controllers;

import com.client.DTO.UserDTO;
import com.client.service.ApiClient;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final ApiClient apiClient;

    public AuthController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }


    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO user,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            apiClient.register(user);
            return "redirect:/login?registered";
        } catch (Exception e) {
            result.reject("error", "Registration failed");
            return "register";
        }
    }
}
