package com.link.hello.controller;

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
        for (String cookie : cookies) {
            cookie = cookie.trim();
            System.out.println(cookie);
            String[] cookieParts = cookie.split("=");
            String cookieKey = cookieParts[0];
            String cookieValue = cookieParts[1];
            if (cookieKey.equals("autentificat") && cookieValue.equals("da")) {
                gasit = true;
                break;
            }
        }
        if (!gasit) {
//            return "NU"; // inlocuit cu redirect
            return null;
        }
        ModelAndView mav = new ModelAndView("dashboard");
        return mav;
    }
}
