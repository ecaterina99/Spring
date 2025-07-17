package com.example.shop.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {

    Map<String, String> accounts = new HashMap<>();

    AuthorizationService() {
        generateDummyAccount();
    }

    void generateDummyAccount() {
        accounts.put("ecaterina", "123123");
        accounts.put("dorin", "pass");
        accounts.put("maria", "qwerty");
    }

    String retrievePassword(String username) {
        return accounts.get(username);
    }

    public boolean validCredentials(String username, String password) {
        String storedPassword = retrievePassword(username);
        return storedPassword != null && storedPassword.equals(password);
    }

}
