package com.link.hello.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
    @RequestMapping("/auth")
    public  class LoginController
    {

        @GetMapping("/login")
        public ModelAndView login() {
ModelAndView mv = new ModelAndView("login");
return mv;
        }

        @PostMapping("/process-login")
        public String processLogin(
               @RequestParam String username,
               @RequestParam String parola
        ){
            System.out.println("Am primit "+username+ " si "+ parola);
            return "Am primit "+username+ " si parola"+ parola;
        }
    }

