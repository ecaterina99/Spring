package com.example.shop.controllers;

import com.example.shop.dto.AllProductsDto;
import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.ResponseDTO;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Show all products: GET /product/all
    @GetMapping("/all")
    public AllProductsDto showAllProducts() {

        AllProductsDto allProductsDto = new AllProductsDto();
        List<ProductDTO> productsDTOs = productService.findAll();
        allProductsDto.setProducts(productsDTOs);
        return allProductsDto;
    }

    // Retrieve product: GET /product/{id}
    @GetMapping("/{id}")
    public ResponseDTO showProduct(
            @PathVariable("id") int id
    ) {
        ResponseDTO responseDTO = new ResponseDTO();
        ProductDTO productDTO = productService.findById(id);
        responseDTO.setData(productDTO);
        responseDTO.setSuccess(productDTO != null);
        return responseDTO;
    }

    // Add new product: POST /product/
    @PostMapping("/")
    public ResponseEntity<Object> addProduct(
            @RequestBody ProductDTO productDTO
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ProductDTO productDtoResponse = productService.save(productDTO);
        return ResponseEntity.ok(productDtoResponse != null ? productDtoResponse : new HashMap<>());
    }

   // Update product: PUT /product/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable int id
    ) {
        if (productDTO.getId() != id) {
            productDTO.setId(0);
        }
        ProductDTO productDTOResponse = productService.update(productDTO, id);
        return ResponseEntity.ok(Objects.requireNonNullElseGet(productDTOResponse, HashMap::new));
    }


    // Delete product: DELETE /product/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(
            @PathVariable int id
    ) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }
        boolean wasDeleted = productService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.ok(new HashMap<>());
        }
        return ResponseEntity.badRequest().build();
    }
}

