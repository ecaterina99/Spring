package com.example.shop.service;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepositoryCrud;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class ProductService {
    private final ProductRepositoryCrud productRepositoryCrud;
    private final DTOManager dtoManager;

    public ProductService(ProductRepositoryCrud productRepositoryCrud, DTOManager dtoManager) {
        this.productRepositoryCrud = productRepositoryCrud;
        this.dtoManager = dtoManager;
    }

    private ProductDTO productToDto(Product product) {
        return dtoManager.productToDto(product);
    }

    private Product productDtoToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setImage(productDTO.getImage());
        product.setBarcode(productDTO.getBarcode());
        product.setQuantity(productDTO.getQuantity());
        return product;
    }

    public List<ProductDTO> findAll() {
        List<ProductDTO> productDTOList = new ArrayList<>();
        Iterable<Product> iterableProducts = productRepositoryCrud.findAll();
        for (Product product : iterableProducts) {
            ProductDTO productDTO = productToDto(product);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    public ProductDTO findById(int id) {
        Optional<Product> productOptional = productRepositoryCrud.findById(id);
        Product product;
        product = productOptional.orElse(null);
        return productToDto(product);
    }


    public ProductDTO save(ProductDTO productDTO) {
        try {
            Product product = productDtoToProduct(productDTO);
            Product savedProduct = productRepositoryCrud.save(product);
            return productToDto(savedProduct);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }

    public ProductDTO update(ProductDTO productDTO, int id) {
        try {
            Optional<Product> existingProductOpt = productRepositoryCrud.findById(id);
            if (existingProductOpt.isEmpty()) {
                return null;
            }

            Product existingProduct = existingProductOpt.get();

            if (productDTO.getName() != null && !productDTO.getName().trim().isEmpty()) {
                existingProduct.setName(productDTO.getName());
            }
            if (productDTO.getDescription() != null && !productDTO.getDescription().trim().isEmpty()) {
                existingProduct.setDescription(productDTO.getDescription());
            }
            if (productDTO.getCategory() != null) {
                existingProduct.setCategory(productDTO.getCategory());
            }
            if (productDTO.getImage() != null) {
                existingProduct.setImage(productDTO.getImage());
            }
            if (productDTO.getBarcode() != null && !productDTO.getBarcode().trim().isEmpty()) {
                existingProduct.setBarcode(productDTO.getBarcode());
            }
            if (productDTO.getQuantity() >= 0) {
                existingProduct.setQuantity(productDTO.getQuantity());
            }
            if (productDTO.getPrice() > 0) {
                existingProduct.setPrice(productDTO.getPrice());
            }

            Product savedProduct = productRepositoryCrud.save(existingProduct);
            return productToDto(savedProduct);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        productRepositoryCrud.deleteById(id);
        Optional<Product> productOptional = productRepositoryCrud.findById(id);
        return productOptional.isEmpty();
    }

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = productToDto(product);
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) return null;

        try {
            String uploadDir = "src/main/resources/static/innerFolder/bouquets/";
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filepath = Paths.get(uploadDir + filename);

            Files.createDirectories(filepath.getParent());
            Files.copy(imageFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            return "/innerFolder/bouquets/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

}
