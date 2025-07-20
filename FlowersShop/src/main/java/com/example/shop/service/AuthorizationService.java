package com.example.shop.service;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.model.Buyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationService {

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private BuyerService buyerService;

    public boolean validCredentials(String email, String password) {
        BuyerDTO userDB = buyerService.findByEmail(email);

        if (userDB == null || !userDB.getEmail().equals(email)) {
            return false;
        }
        return passwordService.verifyPassword(password, userDB.getPasswordHash());
    }

    public boolean isAuthenticated(String auth, String email) {
        if ("no".equals(auth) || "guest".equals(email) ||
                email == null || email.trim().isEmpty()) {
            return false;
        }
        try {
            BuyerDTO buyer = buyerService.findByEmail(email);
            return buyer != null;
        } catch (Exception e) {
            System.err.println("Error checking authentication for email: " + email + ", error: " + e.getMessage());
            return false;
        }
    }

    public Buyer registerUser(
            String firstName,
            String lastName,
            String phone,
            String country,
            String city,
            String address,
            String postalCode,
            String email,
            String password
    ) {
        BuyerDTO existingUser = buyerService.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("User with this email already exists.");
        }

        if (!passwordService.isPasswordStrong(password)) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        String hashedPassword = passwordService.hashPassword(password);

        Buyer newUser = new Buyer();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhone(phone);
        newUser.setCountry(country);
        newUser.setCity(city);
        newUser.setAddress(address);
        newUser.setPostalCode(postalCode);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);

         return buyerService.register(newUser);

    }

}
