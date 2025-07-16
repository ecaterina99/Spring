package com.link.EmployeesWebApp.controller;

import com.link.EmployeesWebApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

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
        authCookies.add("autentificat=da; Path=/; Expires=" + yesterdayStr);
        authCookies.add("username=; Path=/; Expires=" + yesterdayStr);
        headers.put("Set-Cookie", authCookies);

        List<String> locations = new ArrayList<>();
        locations.add("/auth/login");
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
            List<String> cookies = new ArrayList<>();
            cookies.add("autentificat=da; Path=/");
            cookies.add("username=" + username + "; Path=/");
            headers.put("Set-Cookie", cookies);
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

