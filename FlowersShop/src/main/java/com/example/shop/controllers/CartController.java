package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.ViewUtils;
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

/**
 * Handles all shopping cart operations: view, add, update, remove, and checkout.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    private static final String CART_SESSION_KEY = "cartItems";
    private static final String AUTH_YES = "yes";
    private static final String GUEST_EMAIL = "guest";

    private final ProductService productService;
    private final SaleService saleService;
    private final UserService userService;
    private final ViewUtils viewUtils;

    public CartController(ProductService productService, ViewUtils viewUtils, SaleService saleService, UserService userService) {
        this.productService = productService;
        this.saleService = saleService;
        this.userService = userService;
        this.viewUtils = viewUtils;
    }

    /**
     * Displays the cart page with all items and total price
     */
    @GetMapping("/")
    public ModelAndView showCart(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role,
            HttpSession session) {
        List<Cart> cartItems = getCartFromSession(session);
        ModelAndView modelAndView = new ModelAndView("cart");

        modelAndView.addObject("cartItems", cartItems);
        modelAndView.addObject("totalPrice", calculateTotal(cartItems));
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);

        return modelAndView;
    }

    /**
     * Adds new product in the cart
     */
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
            List<Cart> cartItems = getCartFromSession(session);
            // Check total quantity (existing + new) doesn't exceed stock
            int currentQuantityInCart = getCurrentQuantityInCart(cartItems, productId);
            if (product.getQuantity() < (currentQuantityInCart + quantity)) {
                return createErrorResponse("Not enough stock. Available: " +
                        (product.getQuantity() - currentQuantityInCart));
            }
            // Add or update item in cart
            addOrUpdateCartItem(cartItems, productId, product, quantity);
            session.setAttribute(CART_SESSION_KEY, cartItems);

            return ResponseEntity.ok(createSuccessResponse(
                    "Product added to cart",
                    cartItems,
                    productId,
                    getCurrentQuantityInCart(cartItems, productId)
            ));

        } catch (Exception e) {
            return createErrorResponse("Failed to add product to cart");
        }
    }

    /**
     * Updates quantity of a specific item in cart via AJAX
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            HttpSession session) {

        try {
            List<Cart> cartItems = getCartFromSession(session);
            if (cartItems.isEmpty()) {
                return createErrorResponse("Cart is empty");
            }
            // Remove item if quantity is 0
            if (quantity == 0) {
                cartItems.removeIf(item -> item.getProductId().equals(productId));
                session.setAttribute(CART_SESSION_KEY, cartItems);
                return ResponseEntity.ok(createSuccessResponse("Item removed", cartItems, productId, 0));
            }
            // Validate stock availability
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return createErrorResponse("Product not found");
            }
            if (quantity > product.getQuantity()) {
                return createErrorResponse("Not enough stock. Available: " + product.getQuantity());
            }

            // Update quantity
            Optional<Cart> cartItem = cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
                session.setAttribute(CART_SESSION_KEY, cartItems);
                return ResponseEntity.ok(createSuccessResponse(
                        "Quantity updated",
                        cartItems,
                        productId,
                        quantity
                ));
            } else {
                return createErrorResponse("Product not found in cart");
            }
        } catch (Exception e) {
            return createErrorResponse("Failed to update quantity");
        }
    }

    /**
     * Removes a product from cart
     */
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {
        try {
            List<Cart> cartItems = getCartFromSession(session);
            cartItems.removeIf(item -> item.getProductId().equals(productId));
            session.setAttribute(CART_SESSION_KEY, cartItems);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum()
            ));

        } catch (Exception e) {
            return createErrorResponse("Failed to remove product from cart");
        }
    }

    //Processes checkout for authenticated users
    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(
            @CookieValue(name = "authenticated", defaultValue = "no") String authenticated,
            @CookieValue(name = "email", defaultValue = GUEST_EMAIL) String email,
            HttpSession session) {

        try {
            // Check authentication
            if (!AUTH_YES.equals(authenticated) || GUEST_EMAIL.equals(email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required", "redirect", "/auth/login"));
            }
            // Validate cart
            List<Cart> cartItems = getCartFromSession(session);
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Cart is empty"));
            }
            // Get user
            UserDTO user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found", "redirect", "/auth/login"));
            }

            // Process purchase
            List<Integer> sales = saleService.processPurchase(user.getId(), cartItems);

            // Clear cart after successful purchase
            session.removeAttribute(CART_SESSION_KEY);

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

    //HELPERS

    // Returns current cart count for AJAX requests (for cart counter)
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(HttpSession session) {
        List<Cart> cartItems = getCartFromSession(session);
        int count = cartItems.stream().mapToInt(Cart::getQuantity).sum();
        return ResponseEntity.ok(Map.of("count", count));
    }


    //Adds new item or updates existing item quantity in cart
    private void addOrUpdateCartItem(List<Cart> cartItems, Integer productId,
                                     ProductDTO product, Integer quantity) {
        Optional<Cart> existingItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            Cart item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Cart newItem = new Cart();
            newItem.setProductId(productId);
            newItem.setName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(quantity);
            cartItems.add(newItem);
        }
    }

    // Calculates total price of all items in cart
    private double calculateTotal(List<Cart> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    //Creates standardized error response
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", message));
    }

    // Retrieves cart items from session, returns empty list if none found
    private List<Cart> getCartFromSession(HttpSession session) {
        List<Cart> cartItems = (List<Cart>) session.getAttribute(CART_SESSION_KEY);
        return cartItems != null ? cartItems : new ArrayList<>();
    }

    //Gets current quantity of a specific product in cart
    private int getCurrentQuantityInCart(List<Cart> cartItems, Integer productId) {
        return cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .mapToInt(Cart::getQuantity)
                .sum();
    }

    //Creates success response with cart data
    private Map<String, Object> createSuccessResponse(String message, List<Cart> cartItems,
                                                      Integer productId, Integer quantity) {
        return Map.of(
                "success", true,
                "message", message,
                "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum(),
                "totalPrice", calculateTotal(cartItems),
                "productId", productId,
                "quantity", quantity
        );
    }
}
