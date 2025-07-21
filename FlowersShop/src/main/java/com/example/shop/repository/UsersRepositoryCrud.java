package com.example.shop.repository;

import com.example.shop.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepositoryCrud extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}