package com.example.shop.controllers;

import com.example.shop.dto.AllProductsDto;
import com.example.shop.dto.ProductDTO;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

   @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public AllProductsDto showAllProducts() {

        AllProductsDto allProductsDto = new AllProductsDto();
        List<ProductDTO> productsDTOs = productService.findAll();
        allProductsDto.setProducts(productsDTOs);
        return allProductsDto;
    }
}
