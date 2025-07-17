package com.example.shop.repository;

import com.example.shop.model.Buyer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyersRepositoryCrud extends CrudRepository<Buyer, Integer> {

    Optional<Buyer> findByEmail(String email);

}