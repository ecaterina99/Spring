package com.example.shop.controllers;

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
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders headers = new HttpHeaders();
        List<String> authCookies = new ArrayList<>();
        String yesterdayStr = "Tue, 29 Oct 1970 16:56:32 GMT";
        authCookies.add("authenticated=yes; Path=/; Expires=" + yesterdayStr);
        authCookies.add("username=; Path=/; Expires=" + yesterdayStr);
        headers.put("Set-Cookie", authCookies);

        List<String> locations = new ArrayList<>();
        locations.add("/auth/login");
        headers.put("Location", locations);

        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/process-login")
    public ResponseEntity<String> processLogin(
            @RequestParam String username,
            @RequestParam String password
    ) {
        boolean valid = authService.validCredentials(username, password);
        System.out.println("Received " + username + " and " + password + (valid ? " (valid)" : " (invalid)"));
        HttpHeaders headers = new HttpHeaders();
        List<String> locations = new ArrayList<>();
        if (valid) {
            List<String> cookies = new ArrayList<>();
            cookies.add("authenticated=yes; Path=/");
            cookies.add("username=" + username + "; Path=/");
            headers.put("Set-Cookie", cookies);
            // redirect la welcome.html
            locations.add("/account/welcome");
        } else {
            // redirect la login
            locations.add("/auth/login");
        }
        headers.put("Location", locations);

        return new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);
    }
}
