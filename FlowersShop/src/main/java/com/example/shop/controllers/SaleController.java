/*package com.example.shop.controllers;
import com.example.shop.helpers.ViewUtils;
import com.example.shop.model.Sale;
import com.example.shop.service.SaleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
/**
 * REST Controller for sales-related operations
 */
/*@RestController
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;
    private final ViewUtils viewUtils;

    public SaleController(SaleService saleService, ViewUtils viewUtils) {
        this.saleService = saleService;
        this.viewUtils = viewUtils;
    }
    /**
     * Retrieves and displays all sales records
     * @return ModelAndView with sales list
     */

   /*

    @GetMapping("/")
    public ModelAndView listSales(@CookieValue(name = "authenticated", defaultValue = "no") String auth,
                                  @CookieValue(name = "email", defaultValue = "guest") String email,
                                  @CookieValue(name = "role", defaultValue = "buyer") String role) {
        List<Sale> sales = saleService.findAll();
        ModelAndView modelAndView = new ModelAndView("sales/sales","allSales",sales);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }
}


    */