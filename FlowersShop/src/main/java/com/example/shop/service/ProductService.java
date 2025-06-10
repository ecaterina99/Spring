package com.example.shop.service;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ProductRepositoryCrud;
import com.example.shop.repository.ProductRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepositoryCrud productRepositoryCrud;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductRepositoryJpa productRepositoryJpa;


    @Autowired
    private DTOManager dtoManager;

    private ProductDTO productToDto(Product product) {
        return dtoManager.productToDto(product);
    }

    private Product productDtoToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setQuantity(product.getQuantity());
        return product;
    }


    private List<ProductDTO> productsToDTOs(List<Product> products) {
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = productToDto(product);
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }


    public List<ProductDTO> findAll() {
        List<ProductDTO> productDTOList = new ArrayList<>();
        Iterable<Product> iterableProducts = productRepositoryCrud.findAll();
        Iterator<Product> iteratorProducts = iterableProducts.iterator();
        while (iteratorProducts.hasNext()) {
            Product product = iteratorProducts.next();
            ProductDTO productDTO = productToDto(product);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }


}
