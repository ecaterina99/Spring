package com.example.shop.controllers;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.model.Buyer;
import com.example.shop.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthorizationService authService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");

    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders headers = new HttpHeaders();
        String yesterdayStr = "Tue, 29 Oct 1970 16:56:32 GMT";

        headers.add("Set-Cookie", "authenticated=; Path=/; Expires=" + yesterdayStr);
        headers.add("Set-Cookie", "email=; Path=/; Expires=" + yesterdayStr);
        headers.add("Location", "/auth/login");

        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/process-login")
    public ResponseEntity<String> processLogin(
            @RequestParam String email,
            @RequestParam String password
    ) {
        boolean valid = authService.validCredentials(email, password);
        System.out.println("Received " + email + " and " + password + (valid ? " (valid)" : " (invalid)"));
        HttpHeaders headers = new HttpHeaders();

        if (valid) {
            headers.add("Set-Cookie", "authenticated=yes; Path=/");
            headers.add("Set-Cookie", "email=" + email + "; Path=/");
            headers.add("Location", "/account/welcome");
        } else {
            headers.add("Location", "/auth/login");
        }
        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }


    @PostMapping("/process-register")
    public ResponseEntity<String> processRegister(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String phone,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam String postalCode,
            @RequestParam String email,
            @RequestParam String password
    ) {
        HttpHeaders headers = new HttpHeaders();


        try {
            Buyer user = authService.registerUser(name,surname,phone,country,city,address,postalCode, email, password);
            System.out.println("User registered successfully: " + user.getEmail());

            headers.add("Set-Cookie", "authenticated=yes; Path=/");
            headers.add("Set-Cookie", "email=" + email + "; Path=/");
            headers.add("Location", "/account/welcome");
        } catch (RuntimeException e) {
            System.err.println("Registration error: " + e.getMessage());
            headers.add("Location", "/auth/register?error=" + e.getMessage());
        }
        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }
}
