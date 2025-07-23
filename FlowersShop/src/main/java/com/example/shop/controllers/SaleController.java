package com.example.shop.controllers;
import com.example.shop.model.Sale;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView listSales() {
        List<Sale> sales = saleService.findAll();
        return new ModelAndView("sales/sales", "allSales", sales);
    }
}