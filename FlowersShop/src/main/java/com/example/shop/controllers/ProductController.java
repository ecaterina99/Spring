package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserAttributes userAttributes;

    @Autowired
    public ProductController(UserAttributes userAttributes, ProductService productService) {
        this.userAttributes = userAttributes;
        this.productService = productService;
    }

    @GetMapping("/bouquets")
    public String bouquets(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("bouquet"));
        } else if (sort.equalsIgnoreCase("priceDesc")) {
            products = productService.orderByPriceDesc(Product.Category.valueOf("bouquet"));
        } else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "bouquets";
    }

    @GetMapping("/plants")
    public String plants(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("plant"));
        } else if (sort.equalsIgnoreCase("priceDesc")) {
            products = productService.orderByPriceDesc(Product.Category.valueOf("plant"));
        } else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "plants";
    }

    @GetMapping("/gifts")
    public String gifts(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("gift"));
        } else if (sort.equalsIgnoreCase("priceDesc")) {
            products = productService.orderByPriceDesc(Product.Category.valueOf("gift"));
        } else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "gifts";
    }

    @GetMapping("/info/{id}")
    public String showProduct(@PathVariable("id") int id, Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        ProductDTO productDTO = productService.findById(id);
        List<ProductDTO> randomGifts = productService.getRandomProducts();

        model.addAttribute("product", productDTO);
        model.addAttribute("allProducts", randomGifts);
        return "info";
    }
}
