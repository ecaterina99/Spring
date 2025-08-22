package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * Controller for user management operations
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserAttributes userAttributes;

    public UserController(UserService userService, UserAttributes userAttributes) {
        this.userService = userService;
        this.userAttributes = userAttributes;
    }
    @GetMapping("/")
    public String listCustomers(Authentication authentication, Model model) {
        List<UserDTO> usersDTO = userService.findAllUsers();
        model.addAttribute("allUsers", usersDTO);
        userAttributes.addUserAttributes(model, authentication);
        return "users/users";
    }
    @GetMapping("/{id}")
    public String showUser( @PathVariable("id") int id,Authentication authentication, Model model) {
        UserDTO userDTO = userService.findById(id);
        model.addAttribute("user", userDTO);
        userAttributes.addUserAttributes(model, authentication);
        return "users/view";
    }
}
