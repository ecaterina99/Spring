package com.example.shop.helpers;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthEventHandler {

    private static final String CART_SESSION_KEY = "cartItems";

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(CART_SESSION_KEY);
                System.out.println("Cart cleared after successful authentication for user: " + event.getAuthentication().getName());
            }
        } catch (Exception e) {
            System.err.println("Error clearing cart after authentication: " + e.getMessage());
        }
    }
}