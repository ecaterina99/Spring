package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.AuthUtils;
import com.example.shop.helpers.ViewUtils;
import com.example.shop.model.Sale;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final SaleService saleService;
    private final UserService userService;
    private final ViewUtils viewUtils;

    public AdminController(ProductService productService, ViewUtils viewUtils, SaleService saleService, UserService userService) {
        this.productService = productService;
        this.saleService = saleService;
        this.userService = userService;
        this.viewUtils = viewUtils;
    }
    /**
     * Displays admin dashboard with statistics and data overview
     */
    @GetMapping("/dashboard")
    public ModelAndView dashboard(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "role", defaultValue = "buyer") String role,
            @CookieValue(name = "email", defaultValue = "guest") String email
    ) {
        // Security check - redirect non-admin users
        if (!AuthUtils.isAdmin(auth, role)) {
            return new ModelAndView("redirect:/auth/login");
        }
        ModelAndView modelAndView = new ModelAndView("dashboard");
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);

        // Load dashboard data
        List<ProductDTO> allProducts = productService.findAll();
        List<Sale> allSales = saleService.findAll();
        List<UserDTO> allUsers = userService.findAllUsers();

        modelAndView.addObject("allProducts", allProducts);
        modelAndView.addObject("allSales", allSales);
        modelAndView.addObject("allUsers", allUsers);

        return modelAndView;
    }
}