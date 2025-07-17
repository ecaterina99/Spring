package com.example.shop.helpers;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.dto.ProductDTO;
import com.example.shop.model.Buyer;
import com.example.shop.model.Product;
import com.example.shop.model.Sale;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public BuyerDTO buyerToDto(Buyer buyer) {
        return buyerToDto(buyer, true);
    }

    public BuyerDTO buyerToDto(Buyer buyer,boolean deep) {
        if (buyer == null) return null;
        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setId(buyer.getId());
        buyerDTO.setFullName(buyer.getFirstName()+" "+buyer.getLastName());
        buyerDTO.setEmail(buyer.getEmail());
        buyerDTO.setPhone(buyer.getPhone());
        buyerDTO.setAddress(buyer.getAddress());
        buyerDTO.setCity(buyer.getCity());
        buyerDTO.setPostalCode(buyer.getPostalCode());
        buyerDTO.setPasswordHash(buyer.getPasswordHash());

        if(deep){
            List<Sale>sales = buyer.getSales();
            buyerDTO.setSales(sales);
        }
        return buyerDTO;
    }
}
