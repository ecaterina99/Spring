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
