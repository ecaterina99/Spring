package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.model.Cart;
import com.example.shop.model.Sale;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView showCart(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role) {

        ModelAndView modelAndView = new ModelAndView("cart");

        modelAndView.addObject("isAuthenticated", "yes".equals(auth));
        if ("yes".equals(auth) && !"guest".equals(email)) {
            UserDTO user = userService.findByEmail(email);
            if (user != null) {
                modelAndView.addObject("fullName", user.getFullName());
            }
        }
        modelAndView.addObject("role", role);

        return modelAndView;
    }

    @GetMapping("/product/{id}")
    @ResponseBody
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        try {
            ProductDTO product = productService.findById(id);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(
            @CookieValue(name = "authenticated", defaultValue = "no") String authenticated,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {

        try {
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Product not found"));
            }

            if (product.getQuantity() < quantity) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Not enough stock"));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Product added to cart",
                    "product", Map.of(
                            "id", product.getId(),
                            "name", product.getName(),
                            "price", product.getPrice()
                    )
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(
            @CookieValue(name = "authenticated", defaultValue = "no") String authenticated,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @RequestBody List<Cart> cartItems
    ) {
        if (!"yes".equals(authenticated) || "guest".equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required", "redirect", "/auth/login"));
        }

        try {
            UserDTO user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found", "redirect", "/auth/login"));
            }

            List<Integer> saleIds = new ArrayList<>();

            for (Cart item : cartItems) {
                ProductDTO product = productService.findById(item.getProductId());
                if (product == null) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Product not found: " + item.getName()));
                }
                if (product.getQuantity() < item.getQuantity()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Not enough stock for: " + item.getName()));
                }
            }

            for (Cart item : cartItems) {
                Sale sale = saleService.makePurchase(user.getId(), item.getProductId(), item.getQuantity());
                saleIds.add(sale.getId());
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Purchase successful!",
                    "saleIds", saleIds
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Purchase failed: " + e.getMessage()));
        }
    }
}