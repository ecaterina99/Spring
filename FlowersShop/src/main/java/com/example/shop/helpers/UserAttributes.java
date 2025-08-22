package com.example.shop.helpers;

import com.example.shop.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
@Component
public class UserAttributes {

    public void addUserAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            User user = personDetails.getPerson();

            model.addAttribute("isAuthenticated", true);
            model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("role", user.getRole().replace("ROLE_", "").toLowerCase());
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("fullName", null);
            model.addAttribute("role", null);
        }
    }
}
