package com.example.shop.service;

import com.example.shop.dto.ProductDTO;
import com.example.shop.model.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    //Adds new item or updates existing item quantity in cart
    public void addOrUpdateCartItem(List<Cart> cartItems, Integer productId,
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
    public double calculateTotal(List<Cart> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    //Creates standardized error response
    public ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", message));
    }

    //Gets current quantity of a specific product in cart
    public int getCurrentQuantityInCart(List<Cart> cartItems, Integer productId) {
        return cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .mapToInt(Cart::getQuantity)
                .sum();
    }

    //Creates success response with cart data
    public Map<String, Object> createSuccessResponse(String message, List<Cart> cartItems,
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
