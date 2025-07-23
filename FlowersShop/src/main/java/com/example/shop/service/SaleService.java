package com.example.shop.service;

import com.example.shop.dto.SalesDTO;
import com.example.shop.model.User;
import com.example.shop.model.Product;
import com.example.shop.model.Sale;
import com.example.shop.repository.UsersRepositoryCrud;
import com.example.shop.repository.ProductRepositoryCrud;
import com.example.shop.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    private UsersRepositoryCrud usersRepositoryCrud;
    @Autowired
    private ProductRepositoryCrud productRepositoryCrud;

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale makePurchase(int userId, int productId, int quantity) {
        User user = usersRepositoryCrud.findById(userId).orElseThrow();
        Product product = productRepositoryCrud.findById(productId).orElseThrow();

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Out of stock");
        }
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setProduct(product);
        sale.setQuantity(quantity);
        Sale savedSale = saleRepository.save(sale);

        product.setQuantity(product.getQuantity() - quantity);
        productRepositoryCrud.save(product);

        return savedSale;
    }

}
