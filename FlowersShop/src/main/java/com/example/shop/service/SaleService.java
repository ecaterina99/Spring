
package com.example.shop.service;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.model.Cart;
import com.example.shop.model.User;
import com.example.shop.model.Product;
import com.example.shop.model.Sale;
import com.example.shop.repository.UsersRepositoryCrud;
import com.example.shop.repository.ProductRepositoryCrud;
import com.example.shop.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SaleService {
    private final UserService userService;
    private final ProductService productService;
    private final SaleRepository saleRepository;
    private final UsersRepositoryCrud usersRepository;
    private final ProductRepositoryCrud productRepository;

    public SaleService(UserService userService, ProductService productService, SaleRepository saleRepository, UsersRepositoryCrud usersRepository, ProductRepositoryCrud productRepository) {
        this.userService = userService;
        this.productService = productService;
        this.saleRepository = saleRepository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Transactional
    public List<Integer> processPurchase(int userId, List<Cart> cartItems) {
        validateCartItems(cartItems);

        UserDTO user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        List<Integer> saleIds = new ArrayList<>();

        for (Cart item : cartItems) {
            Sale sale = makePurchase(userId, item.getProductId(), item.getQuantity());
            saleIds.add(sale.getId());
        }

        return saleIds;
    }

    private void validateCartItems(List<Cart> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        for (Cart item : cartItems) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for: " + item.getName());
            }

            ProductDTO product = productService.findById(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + item.getName());
            }

            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for: " + item.getName() +
                        ". Available: " + product.getQuantity() + ", requested: " + item.getQuantity());
            }
        }
    }

    @Transactional
    public Sale makePurchase(int userId, int productId, int quantity) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Out of stock for product: " + product.getName() +
                    ". Available: " + product.getQuantity() + ", requested: " + quantity);
        }

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setProduct(product);
        sale.setQuantity(quantity);
        Sale savedSale = saleRepository.save(sale);

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        return savedSale;
    }
}
