package com.example.shop.service;

import com.example.shop.helpers.PersonDetails;
import com.example.shop.model.User;
import com.example.shop.repository.UsersRepositoryCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {


    private final UsersRepositoryCrud usersRepository;

    @Autowired
    public PersonDetailsService( UsersRepositoryCrud usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> person = usersRepository.findByEmail(email);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
}
