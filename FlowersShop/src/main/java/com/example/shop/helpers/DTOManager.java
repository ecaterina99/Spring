package com.example.shop.helpers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.model.Product;
import org.springframework.stereotype.Component;

@Component
public class DTOManager {

    public ProductDTO productToDto(Product product) {
        return productToDto(product, true);
    }

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
        productDTO.setAvailability(isAvailable(product.getQuantity()));

        return productDTO;
    }

    private boolean isAvailable(int quantity) {
        if (quantity <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
