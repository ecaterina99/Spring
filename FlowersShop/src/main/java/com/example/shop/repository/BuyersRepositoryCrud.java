package com.example.shop.repository;

import com.example.shop.model.Buyer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyersRepositoryCrud extends CrudRepository<Buyer, Integer> {

}