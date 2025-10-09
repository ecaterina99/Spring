package com.server.services;

import com.server.models.User;
import com.server.repositories.UserRepository;
import com.server.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository usersRepository;

    @Autowired
    public UserDetailsService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> person = Optional.ofNullable(usersRepository.findByEmail(email));
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetails(person.get());
    }
}
