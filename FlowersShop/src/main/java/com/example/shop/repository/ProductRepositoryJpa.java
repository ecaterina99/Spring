package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryOrderByPriceAsc(Product.Category category);
    List<Product> findAllByCategoryOrderByPriceDesc(Product.Category category);
    List<Product> findProductByCategory(Product.Category category);
}
