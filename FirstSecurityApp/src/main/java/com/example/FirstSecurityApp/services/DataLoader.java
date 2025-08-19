package com.example.FirstSecurityApp.services;

import com.example.FirstSecurityApp.repository.PeopleRepository;
import com.example.FirstSecurityApp.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}

  /*  @Override
    public void run(String... args) throws Exception {
        if (peopleRepository.count() == 0) {
            Person admin = new Person("admin", passwordEncoder.encode("admin123"), 1990);
            Person user = new Person("user", passwordEncoder.encode("user123"), 1995);
            Person alice = new Person("alice", passwordEncoder.encode("alice123"), 1985);

            peopleRepository.saveAll(List.of(admin, user, alice));
            System.out.println("Users :");
            System.out.println("admin/admin123");
            System.out.println("user/user123");
            System.out.println("alice/alice123");
        }

   */
