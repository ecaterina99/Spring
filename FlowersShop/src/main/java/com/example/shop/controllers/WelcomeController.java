package com.example.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class WelcomeController {

    @GetMapping("/welcome")
    public ModelAndView dashboard(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email) {

        if (!auth.equals("yes")) {
            return new ModelAndView("redirect:/auth/login");
        }
        return new ModelAndView("welcome", "email", email);
    }
}
