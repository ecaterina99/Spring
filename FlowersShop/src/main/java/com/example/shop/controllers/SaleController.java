package com.example.shop.controllers;

import com.example.shop.dto.SalesDTO;
import com.example.shop.model.Sale;
import com.example.shop.repository.UsersRepositoryCrud;
import com.example.shop.repository.ProductRepositoryCrud;
import com.example.shop.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private UsersRepositoryCrud usersRepositoryCrud;
    @Autowired
    private ProductRepositoryCrud productRepositoryCrud;


    @GetMapping("/all")
    public List<Sale> getAllSales() {
        return saleService.findAll();
    }


    @PostMapping("/request")
    public ResponseEntity<String> makePurchase(@RequestBody SalesDTO request) {
        try {
            saleService.makePurchase(request.getUserId(), request.getProductId(), request.getQuantity()
            );
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
