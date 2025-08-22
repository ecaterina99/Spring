package com.example.shop.controllers;

import com.example.shop.dto.UserDTO;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
/**
 * Controller for user management operations
 */
/*@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ViewUtils viewUtils;


    public UserController(UserService userService,ViewUtils viewUtils) {
        this.userService = userService;
        this.viewUtils = viewUtils;
    }
    /**
     * Displays list of all registered users
     * @return ModelAndView with users list
     */
   /* @GetMapping("/")
    public ModelAndView listCustomers(@CookieValue(name = "authenticated", defaultValue = "no") String auth,
                                      @CookieValue(name = "email", defaultValue = "guest") String email,
                                      @CookieValue(name = "role", defaultValue = "buyer") String role) {
        List<UserDTO> usersDTO = userService.findAllUsers();
        ModelAndView modelAndView = new ModelAndView("users/users", "allUsers", usersDTO);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView showUser(
            @PathVariable("id") int id,
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        UserDTO userDTO = userService.findById(id);
        ModelAndView modelAndView = new ModelAndView("users/view", "user", userDTO);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }
}

    */