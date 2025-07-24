package com.example.shop.helpers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ViewUtils {

    private final UserService userService;

    public ViewUtils(UserService userService) {
        this.userService = userService;
    }

    /**
     * Adds common user authentication data to ModelAndView
     * @param modelAndView the ModelAndView to enhance
     * @param auth authentication status
     * @param email user email
     * @param role user role
     */
    public void addAuthenticationData(ModelAndView modelAndView, String auth, String email, String role) {
        boolean isAuthenticated = AuthUtils.isAuthenticated(auth);

        modelAndView.addObject("isAuthenticated", isAuthenticated);
        modelAndView.addObject("email", email);
        modelAndView.addObject("role", role);

        if (isAuthenticated && !"guest".equals(email)) {
            UserDTO user = userService.findByEmail(email);
            if (user != null) {
                modelAndView.addObject("fullName", user.getFullName());
            }
        }
    }
}