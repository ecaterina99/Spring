package com.example.shop.service;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepositoryCrud;
import com.example.shop.repository.ProductRepositoryJpa;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepositoryCrud productRepositoryCrud;
    private final DTOManager dtoManager;
    private final ProductRepositoryJpa productRepositoryJpa;

    public ProductService(ProductRepositoryCrud productRepositoryCrud, DTOManager dtoManager,  ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryCrud = productRepositoryCrud;
        this.dtoManager = dtoManager;
        this.productRepositoryJpa = productRepositoryJpa;
    }

    //Converts Product entity to ProductDTO
    private ProductDTO productToDto(Product product) {
        return dtoManager.productToDto(product);
    }

    //Converts ProductDTO to Product entity with validation
    private Product productDtoToProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        }
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

    //Retrieves all products and converts them to DTOs
    public List<ProductDTO> findAll() {
        List<ProductDTO> productDTOList = new ArrayList<>();
        Iterable<Product> iterableProducts = productRepositoryCrud.findAll();
        for (Product product : iterableProducts) {
            ProductDTO productDTO = productToDto(product);
            if (productDTO != null) {
                productDTOList.add(productDTO);
            }
        }
        return productDTOList;
    }

    //Finds product by ID and converts to DTO
    public ProductDTO findById(int id) {
        Optional<Product> productOptional = productRepositoryCrud.findById(id);
        return productOptional.map(this::productToDto).orElse(null);
    }

    //Saves a new product with validation
    public ProductDTO save(ProductDTO productDTO) {
        try {
            Product product = productDtoToProduct(productDTO);
            Product savedProduct = productRepositoryCrud.save(product);
            return productToDto(savedProduct);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }

    //Updates existing product with partial data
    public ProductDTO update(ProductDTO productDTO, int id) {
        try {
            Optional<Product> existingProductOpt = productRepositoryCrud.findById(id);
            if (existingProductOpt.isEmpty()) {
                return null;
            }
            Product existingProduct = existingProductOpt.get();
            updateProductFields(existingProduct, productDTO);

            Product savedProduct = productRepositoryCrud.save(existingProduct);
            return productToDto(savedProduct);

        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

    //Helper method to update product fields with null checks
    private void updateProductFields(Product existingProduct, ProductDTO productDTO) {
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
    }

    // Deletes product by ID
    public void delete(int id) {
            productRepositoryCrud.deleteById(id);
    }

    //Saves uploaded image file with UUID naming
    public String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) return null;

        try {
            String uploadDir = "src/main/resources/static/innerFolder/products/";
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filepath = Paths.get(uploadDir + filename);

            Files.createDirectories(filepath.getParent());
            Files.copy(imageFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            return "/innerFolder/products/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public List<ProductDTO> getRandomProducts() {
        List<Product> allProducts = (List<Product>) productRepositoryCrud.findAll();
        List<Product> gifts = new ArrayList<>(allProducts.stream()
                .filter(product -> product.getCategory() != null)
                .filter(product -> product.getCategory().toString().equalsIgnoreCase("gift"))
                .toList());

        Collections.shuffle(gifts);
        int actualLimit = Math.min(gifts.size(), 4);
        List<Product> randomGifts = gifts.subList(0, actualLimit);

        return randomGifts.stream()
                .map(this::productToDto)
                .collect(Collectors.toList());

    }

    public List<ProductDTO> orderByPriceAsc(Product.Category category) {
        List<Product> products = productRepositoryJpa.findAllByCategoryOrderByPriceAsc(category);

        return products.stream()
                .map(this::productToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> orderByPriceDesc(Product.Category category) {
        List<Product> products = productRepositoryJpa.findAllByCategoryOrderByPriceDesc(category);

        return products.stream()
                .map(this::productToDto)
                .collect(Collectors.toList());
    }
}