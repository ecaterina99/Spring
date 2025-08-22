package com.example.shop.controllers;


import com.example.shop.helpers.PersonValidator;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.User;
import com.example.shop.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")

public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final UserAttributes userAttributes;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, UserAttributes userAttributes) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.userAttributes = userAttributes;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("isAuthenticated", false);
        return "login";
    }



    @GetMapping("/register")
    public String registerUser(@ModelAttribute("user") User person, Model model) {
        model.addAttribute("isAuthenticated", false);

        return "register";
    }

    @PostMapping("/process-register")
    public String performRegistration(@ModelAttribute("user") @Valid User person,
                                      BindingResult bindingResult, Model model) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("isAuthenticated", false);
            return "register";
        }

        registrationService.register(person);
        return "redirect:/auth/login?registered";
    }


}