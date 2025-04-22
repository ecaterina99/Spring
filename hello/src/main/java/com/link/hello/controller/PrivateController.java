package com.link.hello.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class PrivateController {
    @RequestMapping(method = RequestMethod.GET, path = "/dashboard")
    public String dashboard(
            @RequestHeader(name = "Cookie", defaultValue = "") String cookie
    ) {
        if (cookie.isEmpty()) {
            return "Nu ai acces!";
        }
        return "Continut disponibil doar utilizatorilor autentificati!";
    }
}
