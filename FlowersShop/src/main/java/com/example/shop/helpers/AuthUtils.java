package com.example.shop.helpers;

import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    /**
     * Checks if user has admin privileges
     * @param auth authentication status from cookie
     * @param role user role from cookie
     * @return true if user is authenticated admin
     */
    public static boolean isAdmin(String auth, String role) {
        return "yes".equals(auth) && "admin".equals(role);
    }

    /**
     * Checks if user is authenticated
     * @param auth authentication status from cookie
     * @return true if user is authenticated
     */
    public static boolean isAuthenticated(String auth) {
        return "yes".equals(auth);
    }

    /**
     * Creates expired cookie header for logout
     * @param cookieName name of the cookie to expire
     * @return formatted cookie header string
     */
    public static String createExpiredCookieHeader(String cookieName) {
        return cookieName + "=; Path=/; Expires=Tue, 29 Oct 1970 16:56:32 GMT";
    }
}