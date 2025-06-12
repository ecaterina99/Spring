package com.example.shop.repository;

import com.example.shop.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyersRepositoryJpa extends JpaRepository<Buyer, Integer> {

    @Query("SELECT b FROM buyers b JOIN b.sales")
    List<Buyer> findAllBuyersAndSales();

}