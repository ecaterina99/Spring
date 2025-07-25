package com.example.shop.controllers;
import com.example.shop.helpers.ViewUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final ViewUtils viewUtils;

    public HomeController(ViewUtils viewUtils) {
        this.viewUtils = viewUtils;
    }

    /**
     * Displays home page with user authentication status
     */
    @GetMapping("/home")
    public ModelAndView home(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        ModelAndView modelAndView = new ModelAndView("home");
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    /**
     * Displays contact page with user authentication status
     */
    @GetMapping("/contact")
    public ModelAndView contactPage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role) {

        ModelAndView modelAndView = new ModelAndView("contact");
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    @GetMapping("/response")
    public ModelAndView responsePage(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role) {

        ModelAndView modelAndView = new ModelAndView("response");
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }
}