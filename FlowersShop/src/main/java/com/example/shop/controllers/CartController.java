package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.Cart;
import com.example.shop.model.User;
import com.example.shop.service.CartService;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Handles all shopping cart operations: view, add, update, remove, and checkout.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    private static final String CART_SESSION_KEY = "cartItems";
    private final ProductService productService;
    private final SaleService saleService;
    private final UserAttributes userAttributes;
    private final CartService cartService;

    public CartController(ProductService productService, SaleService saleService, UserAttributes userAttributes, CartService cartService) {
        this.productService = productService;
        this.saleService = saleService;
        this.userAttributes = userAttributes;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String showCart(Model model, Authentication authentication, HttpSession session) {
        userAttributes.addUserAttributes(model, authentication);
        List<Cart> cartItems = getCartFromSession(session);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotal(cartItems));
        return "cart";
    }

    /**
     * Adds new product in the cart
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(HttpSession session,
                                                         @RequestParam Integer productId,
                                                         @RequestParam Integer quantity) {
        try {
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return cartService.createErrorResponse("Product not found");
            }
            List<Cart> cartItems = getCartFromSession(session);

            // Check total quantity (existing + new) doesn't exceed stock
            int currentQuantityInCart = cartService.getCurrentQuantityInCart(cartItems, productId);
            if (product.getQuantity() < (currentQuantityInCart + quantity)) {
                return cartService.createErrorResponse("Not enough stock!");
            }

            // Add or update item in cart
            cartService.addOrUpdateCartItem(cartItems, productId, product, quantity);
            session.setAttribute(CART_SESSION_KEY, cartItems);

            return ResponseEntity.ok(cartService.createSuccessResponse(
                    "Product added to cart",
                    cartItems,
                    productId,
                    cartService.getCurrentQuantityInCart(cartItems, productId)
            ));

        } catch (Exception e) {
            return cartService.createErrorResponse("Failed to add product to cart");
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
                return cartService.createErrorResponse("Cart is empty");
            }

            // Remove item if quantity is 0
            if (quantity == 0) {
                cartItems.removeIf(item -> item.getProductId().equals(productId));
                session.setAttribute(CART_SESSION_KEY, cartItems);
                return ResponseEntity.ok(cartService.createSuccessResponse("Item removed", cartItems, productId, 0));
            }

            // Validate stock availability
            ProductDTO product = productService.findById(productId);
            if (product == null) {
                return cartService.createErrorResponse("Product not found");
            }
            if (quantity > product.getQuantity()) {
                return cartService.createErrorResponse("Not enough stock. Available: " + product.getQuantity());
            }

            // Update quantity
            Optional<Cart> cartItem = cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
                session.setAttribute(CART_SESSION_KEY, cartItems);

                // Build response and extend it
                Map<String, Object> response = new java.util.HashMap<>(cartService.createSuccessResponse(
                        "Quantity updated",
                        cartItems,
                        productId,
                        quantity
                ));

                // add item subtotal
                response.put("itemSubtotal", cartItem.get().getPrice() * cartItem.get().getQuantity());

                return ResponseEntity.ok(response);
            } else {
                return cartService.createErrorResponse("Product not found in cart");
            }
        } catch (Exception e) {
            return cartService.createErrorResponse("Failed to update quantity");
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
                    "cartCount", cartItems.stream().mapToInt(Cart::getQuantity).sum(),
                    "totalPrice", cartService.calculateTotal(cartItems)
            ));

        } catch (Exception e) {
            return cartService.createErrorResponse("Failed to remove product from cart");
        }
    }

    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(Authentication authentication, HttpSession session) {
        try {
            if (!userAttributes.isUserAuthenticated(authentication)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "success", false,
                                "error", "Authentication required",
                                "redirect", "/auth/login",
                                "requireLogin", true
                        ));
            }

            // Validate cart
            List<Cart> cartItems = getCartFromSession(session);
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Cart is empty"));
            }

            // Get user
            User user = userAttributes.getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "success", false,
                                "error", "User not found",
                                "redirect", "/auth/login",
                                "requireLogin", true
                        ));
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
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Purchase failed. Please try again."));
        }
    }

    // Returns current cart count for AJAX requests (for cart counter)
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(HttpSession session) {
        List<Cart> cartItems = getCartFromSession(session);
        int count = cartItems.stream().mapToInt(Cart::getQuantity).sum();
        return ResponseEntity.ok(Map.of("count", count));
    }

    // Retrieves cart items from session, returns empty list if none found
    public List<Cart> getCartFromSession(HttpSession session) {
        List<Cart> cartItems = (List<Cart>) session.getAttribute(CART_SESSION_KEY);
        return cartItems != null ? cartItems : new ArrayList<>();
    }
}

