package com.example.shop.service;

import com.example.shop.model.User;
import com.example.shop.repository.UsersRepositoryCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {


    private final UsersRepositoryCrud peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UsersRepositoryCrud peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(User person) {
        String password = passwordEncoder.encode(person.getPassword());
        person.setRole("ROLE_USER");
        person.setPassword(password);
        peopleRepository.save(person);
    }
}
