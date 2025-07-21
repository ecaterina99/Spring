package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    private boolean isAdmin(String auth, String role) {
        return "yes".equals(auth) && "admin".equals(role);
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "role", defaultValue = "buyer") String role,
            @CookieValue(name = "email", defaultValue = "guest") String email
    ) {
        if (!isAdmin(auth, role)) {
            return new ModelAndView("redirect:/auth/login");
        }

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("isAuthenticated", "yes".equals(auth));
        modelAndView.addObject("role", role);

        UserDTO user = userService.findByEmail(email);
        if (user != null) {
            modelAndView.addObject("fullName", user.getFullName());
        }

        return modelAndView;
    }
}
