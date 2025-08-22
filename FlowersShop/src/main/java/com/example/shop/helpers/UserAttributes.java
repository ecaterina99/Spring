package com.example.shop.helpers;

import com.example.shop.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
@Component
public class UserAttributes {

    public void addUserAttributes(Model model, Authentication authentication) {
        boolean isAuthenticated = isUserAuthenticated(authentication);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            User user = personDetails.getPerson();

            model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("role", user.getRole().replace("ROLE_", "").toLowerCase());
            model.addAttribute("userId", user.getId());
        } else {
            model.addAttribute("fullName", null);
            model.addAttribute("role", null);
            model.addAttribute("userId", null);
        }
    }

    public boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof PersonDetails;
    }

    public User getCurrentUser(Authentication authentication) {
        if (!isUserAuthenticated(authentication)) {
            return null;
        }
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }

}