package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.AuthorizationService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserService userService;

    private void addUserDataToModel(ModelAndView modelAndView, String auth, String email) {
        modelAndView.addObject("email", email);

        if (authorizationService.isAuthenticated(auth, email)) {
            UserDTO user = userService.findByEmail(email);
            if (user != null) {
                modelAndView.addObject("fullName", user.getFullName());
                modelAndView.addObject("isAuthenticated", true);
            } else {
                modelAndView.addObject("email", "guest");
                modelAndView.addObject("isAuthenticated", false);
            }
        } else {
            modelAndView.addObject("isAuthenticated", false);
        }
    }

    @GetMapping("/home")
    public ModelAndView home(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        ModelAndView modelAndView = new ModelAndView("home");

        modelAndView.addObject("isAuthenticated", "yes".equals(auth));

        UserDTO user = userService.findByEmail(email);
        if (user != null) {
            modelAndView.addObject("fullName", user.getFullName());
        }

        modelAndView.addObject("role", role);
        return modelAndView;
    }

    @GetMapping("/bouquets")
    public ModelAndView productsPage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role) {
        ModelAndView modelAndView = new ModelAndView("bouquets");
        addUserDataToModel(modelAndView, auth, email);
        modelAndView.addObject("role", role);
        return modelAndView;
    }

    @GetMapping("/contact")
    public ModelAndView contactPage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role) {
        ModelAndView modelAndView = new ModelAndView("contact");
        addUserDataToModel(modelAndView, auth, email);
        modelAndView.addObject("role", role);
        return modelAndView;
    }

    @GetMapping("/response")
    public ModelAndView responsePage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email) {
        ModelAndView modelAndView = new ModelAndView("response");
        addUserDataToModel(modelAndView, auth, email);
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView cart(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email) {
        ModelAndView modelAndView = new ModelAndView("cart");
        addUserDataToModel(modelAndView, auth, email);
        return modelAndView;
    }

}