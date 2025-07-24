package com.example.shop.controllers;
import com.example.shop.model.Sale;
import com.example.shop.service.SaleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
/**
 * REST Controller for sales-related operations
 */
@RestController
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }
    /**
     * Retrieves and displays all sales records
     * @return ModelAndView with sales list
     */
    @GetMapping("/")
    public ModelAndView listSales() {
        List<Sale> sales = saleService.findAll();
        return new ModelAndView("sales/sales", "allSales", sales);
    }
}