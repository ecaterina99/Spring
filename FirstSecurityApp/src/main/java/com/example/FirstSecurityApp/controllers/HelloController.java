package com.example.FirstSecurityApp.controllers;

import com.example.FirstSecurityApp.repository.PeopleRepository;
import com.example.FirstSecurityApp.models.Person;
import com.example.FirstSecurityApp.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    private final PeopleRepository peopleRepository;

    @Autowired
    public HelloController(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        List<Person> allPersons = peopleRepository.findAll();
        model.addAttribute("allPersons", allPersons);
        return "hello";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
        model.addAttribute("currentUser", personDetails.getPerson());
        System.out.println(personDetails.getPerson());
        return "userInfo";
    }
}
