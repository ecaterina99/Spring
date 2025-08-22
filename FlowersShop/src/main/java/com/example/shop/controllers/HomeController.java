package com.example.shop.controllers;

import com.example.shop.helpers.UserAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
private final UserAttributes userAttributes;

@Autowired
public HomeController(UserAttributes userAttributes) {
    this.userAttributes = userAttributes;
}

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        return "home";
    }

    @GetMapping("/contact")
    public String contact(Model model, Authentication authentication) {
    userAttributes.addUserAttributes(model, authentication);
    return "contact";
    }
}