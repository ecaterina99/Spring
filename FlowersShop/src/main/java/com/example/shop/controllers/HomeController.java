package com.example.shop.controllers;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.service.AuthorizationService;
import com.example.shop.service.BuyerService;
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
    private BuyerService buyerService;
    private void addUserDataToModel(ModelAndView modelAndView, String auth, String email) {
        modelAndView.addObject("email", email);

        if (authorizationService.isAuthenticated(auth, email)) {
            BuyerDTO buyer = buyerService.findByEmail(email);
            if (buyer != null) {
                modelAndView.addObject("fullName", buyer.getFullName());
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
    public ModelAndView mainPage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email) {
        ModelAndView modelAndView = new ModelAndView("home");
        addUserDataToModel(modelAndView, auth, email);
        return modelAndView;
    }
    @GetMapping("/bouquets")
    public ModelAndView productsPage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email) {

        ModelAndView modelAndView = new ModelAndView("bouquets");
        addUserDataToModel(modelAndView, auth, email);
        return modelAndView;
    }
}