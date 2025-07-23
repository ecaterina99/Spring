package com.example.shop.helpers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Security utilities for authentication and authorization
 */
@Component
public final class SecurityUtils {

    /**
     * Check if user has admin role
     */
    public static boolean isAdmin(String auth, String role) {
        return "yes".equals(auth) && "admin".equals(role);
    }

    /**
     * Add authentication data to model view
     */
    public static void addAuthDataToModel(ModelAndView modelAndView, String auth, String role,
                                          String email, UserService userService) {
        boolean isAuthenticated = "yes".equals(auth);
        modelAndView.addObject("isAuthenticated", isAuthenticated);
        modelAndView.addObject("role", role);
        modelAndView.addObject("email", email);

        if (isAuthenticated && !"guest".equals(email)) {
            UserDTO user = userService.findByEmail(email);
            if (user != null) {
                modelAndView.addObject("fullName", user.getFullName());
            }
        }
    }
}
