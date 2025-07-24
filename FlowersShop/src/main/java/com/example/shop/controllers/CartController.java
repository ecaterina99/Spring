package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.model.Cart;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final SaleService saleService;
    private final UserService userService;

    public CartController(ProductService productService, SaleService saleService, UserService userService) {
        this.productService = productService;
        this.saleService = saleService;
        this.userService = userService;
    }


    @GetMapping("/")
    public ModelAndView showCart(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role,
            HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("cart");
        boolean isAuthenticated = "yes".equals(auth);

        modelAndView.addObject("isAuthenticated", isAuthenticated);
        modelAndView.addObject("role", role);

        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        modelAndView.addObject("cartItems", cartItems);
        modelAndView.addObject("totalPrice", calculateTotal(cartItems));

        if (isAuthenticated && !"guest".equals(email)) {
            UserDTO user = userService.findByEmail(email);
            if (user != null) {
                modelAndView.addObject("fullName", user.getFullName());
            }
        }
        return modelAndView;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            HttpSession session) {

        try {
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return createErrorResponse("Product not found");
            }

            if (product.getQuantity() < quantity) {
                return createErrorResponse("Not enough stock");
            }

            List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }

            Optional<Cart> existingItem = cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            } else {
                Cart newItem = new Cart();
                newItem.setProductId(productId);
                newItem.setName(product.getName());
                newItem.setPrice(product.getPrice());
                newItem.setQuantity(quantity);
                cartItems.add(newItem);
            }

            session.setAttribute("cartItems", cartItems);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Product added to cart",
                    "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum()
            ));
        } catch (Exception e) {
            return createErrorResponse("Failed to add product to cart");
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            HttpSession session) {

        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        if (cartItems == null) {
            return createErrorResponse("Cart is empty");
        }

        if (quantity <= 0) {
            cartItems.removeIf(item -> item.getProductId().equals(productId));
            session.setAttribute("cartItems", cartItems);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum(),
                    "totalPrice", calculateTotal(cartItems),
                    "productId", productId,
                    "quantity", 0
            ));
        } else {
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return createErrorResponse("Product not found");
            }

            if (quantity > product.getQuantity()) {
                return createErrorResponse("Not enough stock. Available: " + product.getQuantity());
            }
            Optional<Cart> cartItem = cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
                session.setAttribute("cartItems", cartItems);

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum(),
                        "totalPrice", calculateTotal(cartItems),
                        "productId", productId,
                        "quantity", quantity
                ));
            } else {
                return createErrorResponse("Product not found in cart");
            }
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {

        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getProductId().equals(productId));
            session.setAttribute("cartItems", cartItems);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "cartCount", cartItems != null ? cartItems.stream().mapToInt(Cart::getQuantity).sum() : 0
        ));
    }

    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(
            @CookieValue(name = "authenticated", defaultValue = "no") String authenticated,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            HttpSession session
    ) {
        if (!"yes".equals(authenticated) || "guest".equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required", "redirect", "/auth/login"));
        }

        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Cart is empty"));
        }

        try {
            UserDTO user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found", "redirect", "/auth/login"));
            }

            List<Integer> sales = saleService.processPurchase(user.getId(), cartItems);

            session.removeAttribute("cartItems");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Purchase successful!",
                    "saleIds", sales
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Purchase failed. Please try again."));
        }
    }

    private double calculateTotal(List<Cart> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", message));
    }
}