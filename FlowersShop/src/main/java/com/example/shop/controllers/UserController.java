package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
/**
 * Controller for user management operations
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    /**
     * Displays list of all registered users
     * @return ModelAndView with users list
     */
    @GetMapping("/")
    public ModelAndView listCustomers() {
        List<UserDTO> userDTOs = userService.findAllUsers();
        return new ModelAndView("users/users", "allUsers", userDTOs);
    }

    @GetMapping("/{id}")
    public ModelAndView showUser(
            @PathVariable("id") int id
    ) {
        UserDTO userDTO = userService.findById(id);
        return new ModelAndView("users/view", "user", userDTO);
    }
}