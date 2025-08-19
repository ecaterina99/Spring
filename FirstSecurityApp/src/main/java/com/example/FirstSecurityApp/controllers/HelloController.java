package com.example.FirstSecurityApp.controllers;

import com.example.FirstSecurityApp.repository.PeopleRepository;
import com.example.FirstSecurityApp.models.Person;
import com.example.FirstSecurityApp.security.PersonDetails;
import com.example.FirstSecurityApp.security.RegistrationService;
import com.example.FirstSecurityApp.services.AdminService;
import com.example.FirstSecurityApp.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HelloController {

    private final PeopleRepository peopleRepository;
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final AdminService adminService;

    @Autowired
    public HelloController(PeopleRepository peopleRepository, PersonValidator personValidator, RegistrationService registrationService, AdminService adminService) {
        this.peopleRepository = peopleRepository;
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.adminService = adminService;
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


    @GetMapping("/registration")
    public String registerUser(@ModelAttribute("person") Person person) {
        return "registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/registration";
        }

        registrationService.register(person);
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        adminService.doAdminStaff();
        return "admin";

    }

}
