package com.link.EmployeesWebApp.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service  //prelucreaza anumite date
public class AuthService {

    Map<String, String> accounts = new HashMap<>();

    AuthService() {
        generateDummyAccount();
    }

    void generateDummyAccount() {
        accounts.put("vasilica", "123");
        accounts.put("gigel", "pass");
        accounts.put("maria", "888");
    }

    String retrievePassword(String username) {
        return accounts.get(username);
    }

    public boolean validCredentials(String username, String password) {
        String storedPassword = retrievePassword(username);
        return storedPassword != null && storedPassword.equals(password);
    }

}
