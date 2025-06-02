package com.link.hello.controller;

import com.link.hello.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders headers = new HttpHeaders();
        List<String> authCookies = new ArrayList<>();
        String yesterdayStr = "Tue, 29 Oct 1970 16:56:32 GMT";
        authCookies.add("autentificat=da; Path=/; Expires=" + yesterdayStr);
        headers.put("Set-Cookie", authCookies);

        List<String> locations = new ArrayList<>();
        locations.add("/account/dashboard");
        headers.put("Location", locations);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);

        return responseEntity;
    }

    @PostMapping("/process-login")
    public ResponseEntity<String> processLogin(
            @RequestParam String username,
            @RequestParam String parola
    ) {
        boolean valid = authService.validCredentials(username, parola);
        System.out.println("Am primit " + username + " si " + parola + (valid ? " (valid)" : " (nevalid)"));
        HttpHeaders headers = new HttpHeaders();
        List<String> locations = new ArrayList<>();
        if (valid) {
            List<String> authCookies = new ArrayList<>();
            authCookies.add("autentificat=da; Path=/");
            headers.put("Set-Cookie", authCookies);
            // redirect la dashboard
            locations.add("/account/dashboard");
        } else {
            // redirect la login
            locations.add("/auth/login");
        }
        headers.put("Location", locations);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", headers, HttpStatus.SEE_OTHER);

        return responseEntity;
    }
}

