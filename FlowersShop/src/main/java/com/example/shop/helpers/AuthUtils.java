package com.example.shop.helpers;

import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

   // Checks if user has admin privileges
    public static boolean isAdmin(String auth, String role) {
        return "yes".equals(auth) && "admin".equals(role);
    }

    //Checks if user is authenticated
    public static boolean isAuthenticated(String auth) {
        return "yes".equals(auth);
    }
}