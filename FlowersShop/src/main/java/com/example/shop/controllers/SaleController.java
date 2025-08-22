package com.example.shop.controllers;

import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.Sale;
import com.example.shop.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * REST Controller for sales-related operations
 */
@Controller
@RequestMapping("/sale")
public class SaleController {
    private final UserAttributes userAttributes;
    private final SaleService saleService;

    @Autowired
    public SaleController(UserAttributes userAttributes, SaleService saleService) {
        this.userAttributes = userAttributes;
        this.saleService = saleService;
    }
    /**
     * Retrieves and displays all sales records
     * @return ModelAndView with sales list
     */
    @GetMapping("/")
    public String listSales(Authentication authentication, Model model) {
        List<Sale> sales = saleService.findAll();
        model.addAttribute("allSales", sales);
        userAttributes.addUserAttributes(model, authentication);
        return "sales/sales";
    }
}
