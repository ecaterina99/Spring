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
        return showProductsByCategory("bouquet", sort, "bouquets", model, authentication);
    }

    @GetMapping("/plants")
    public String plants(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        return showProductsByCategory("plant", sort, "plants", model, authentication);
    }

    @GetMapping("/gifts")
    public String gifts(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        return showProductsByCategory("gift", sort, "gifts", model, authentication);
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

    private String showProductsByCategory(String category, String sort, String viewName, Model model,
                                          Authentication authentication) {
        List<ProductDTO> products;
        Product.Category productCategory = Product.Category.valueOf(category);
        switch (sort.toLowerCase()) {
            case "pricedesc" -> products = productService.orderByPriceDesc(productCategory);
            case "price" -> products = productService.orderByPriceAsc(productCategory);
            default -> products = productService.getProductsByCategory(productCategory);
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return viewName;
    }
}
