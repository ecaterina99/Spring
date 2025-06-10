package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCrud extends CrudRepository<Product, Integer> {

}