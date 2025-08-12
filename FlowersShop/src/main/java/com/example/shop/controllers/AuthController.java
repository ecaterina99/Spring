package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.model.User;
import com.example.shop.service.AuthorizationService;
import com.example.shop.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Authentication controller handling login, registration, and logout
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthorizationService authService;
    private final UserService userService;

    public AuthController(AuthorizationService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (error != null) {
            modelAndView.addObject("error", error);
        }
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("register");
        if (error != null) {
            modelAndView.addObject("error", error);
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders headers = new HttpHeaders();
        String expiredDate = "Tue, 29 Oct 1970 16:56:32 GMT";
        // Clear all authentication cookies
        clearCookie(headers, "authenticated", expiredDate);
        clearCookie(headers, "email", expiredDate);
        clearCookie(headers, "role", expiredDate);
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
            UserDTO userDTO = userService.findByEmail(email);
            setAuthenticationCookies(headers, email, userDTO.getRole().name());

            if (userDTO.getRole() == User.Role.admin) {
                headers.add("Location", "/admin/dashboard");
            } else if (userDTO.getRole() == User.Role.buyer) {
                headers.add("Location", "/home");
            }
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
            User user = authService.registerUser(name, surname, phone, country, city, address, postalCode, email, password);
            System.out.println("User registered successfully: " + user.getEmail());
            setAuthenticationCookies(headers, email, user.getRole().name());
            headers.add("Location", "/home");
        } catch (RuntimeException e) {
            System.err.println("Registration error: " + e.getMessage());
            headers.add("Location", "/auth/register?error=" + e.getMessage());
        }
        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }

    private void setAuthenticationCookies(HttpHeaders headers, String email, String role) {
        headers.add("Set-Cookie", "authenticated=yes; Path=/");
        headers.add("Set-Cookie", "email=" + email + "; Path=/");
        headers.add("Set-Cookie", "role=" + role + "; Path=/");
    }

    private void clearCookie(HttpHeaders headers, String cookieName, String expiredDate) {
        headers.add("Set-Cookie", cookieName + "=; Path=/; Expires=" + expiredDate);
    }

}
