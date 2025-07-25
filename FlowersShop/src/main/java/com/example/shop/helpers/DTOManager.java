package com.example.shop.helpers;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.ProductDTO;
import com.example.shop.model.User;
import com.example.shop.model.Product;
import com.example.shop.model.Sale;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DTOManager {

    // Converts Product entity to ProductDTO with availability calculation
    public ProductDTO productToDto(Product product) {
        return productToDto(product, true);
    }

    // Converts Product entity to ProductDTO with optional deep conversion
    public ProductDTO productToDto(Product product, boolean deep) {
        if (product == null) return null;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(String.valueOf(product.getCategory()));
        productDTO.setPrice(product.getPrice());
        productDTO.setBarcode(product.getBarcode());
        productDTO.setImage(product.getImage());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setAvailability(product.getQuantity() > 0);

        return productDTO;
    }

    //Converts User entity to UserDTO
    public UserDTO userToDto(User user) {
        return userToDto(user, true);
    }

    public UserDTO userToDto(User user, boolean deep) {
        if (user == null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setCity(user.getCity());
        userDTO.setPostalCode(user.getPostalCode());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setRole(user.getRole());

        if (deep) {
            List<Sale> sales = user.getSales();
            userDTO.setSales(sales);
        }
        return userDTO;
    }
}
