package com.link.EmployeesWebApp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/account")
public class PrivateController {
    @RequestMapping(method = RequestMethod.GET, path = "/dashboard")
    public ModelAndView dashboard(
            @RequestHeader(name = "Cookie", defaultValue = "") String cookieHeader
    ) {
        if (cookieHeader.isEmpty()) {
//            return "Nu ai acces!"; // TODO: redirect la login
            return null;
        }

        String[] cookies = cookieHeader.split(";");
        boolean gasit = false;
        String authenticatedUser = "";
        for (String cookie : cookies) {
            cookie = cookie.trim();
            System.out.println(cookie);
            String[] cookieParts = cookie.split("=");
            String cookieKey = cookieParts[0];
            String cookieValue = cookieParts[1];
            if (cookieKey.equals("autentificat") && cookieValue.equals("da")) {
                gasit = true;
            }
            if(cookieKey.equals("username")) {
                authenticatedUser = cookieValue;
            }
        }
        if(authenticatedUser.isEmpty()) {
            authenticatedUser = "unknown";
        }
        if (!gasit) {
            return null;
        }
        ModelAndView mav = new ModelAndView("dashboard","name","Vasilica");
        return mav;
    }
}
