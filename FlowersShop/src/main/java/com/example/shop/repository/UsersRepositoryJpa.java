package com.example.shop.repository;

import com.example.shop.model.Product;
import com.example.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepositoryJpa extends JpaRepository<User, Integer> {

    @Query("SELECT b FROM users b JOIN b.sales")
    List<User> findAllUsersAndSales();


}