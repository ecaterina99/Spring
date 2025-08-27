package com.example.shop.helpers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


    @Component
    public class CartEventHandler implements AuthenticationSuccessHandler, LogoutHandler {

        public static final String CART_SESSION_KEY = "cartItems";

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) {
            HttpSession session = request.getSession(true);
            if (session != null) {
                session.removeAttribute(CART_SESSION_KEY);
            }
        }

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response,
                           Authentication authentication) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(CART_SESSION_KEY);
            }
        }
    }

