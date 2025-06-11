package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<Product, Long> {

   /* // Поиск по категории
    List<Product> findByCategory(String category);

    // Поиск по категории (игнорируя регистр)
    List<Product> findByCategoryIgnoreCase(String category);

    // Поиск по имени (содержит подстроку)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Подсчет товаров по категории
    long countByCategory(String category);

    */
}