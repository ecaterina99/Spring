package com.example.shop.controllers;


import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.ResponseDTO;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public ModelAndView listProducts() {
    List<ProductDTO> productsDTO = productService.findAll();
    return new ModelAndView("products/products","allProducts",productsDTO);
    }

    @GetMapping("/{id}")
    public ModelAndView showProduct(
            @PathVariable("id") int id
    ) {
        ProductDTO productDTO = productService.findById(id);
        return new ModelAndView("products/view", "product", productDTO);
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView updateProduct(@PathVariable("id") int id,
                                      @RequestParam(value="name",required = false)String name,
                                      @RequestParam(value="description",required = false)String description,
                                      @RequestParam(value="category",required = false) Product.Category category,
                                      @RequestParam(value="price",required = false)Double price,
                                      @RequestParam(value="barcode",required = false)String barcode,
                                      @RequestParam(value="quantity",required = false)Integer quantity,
                                      @RequestParam(value="availability",required = false)Boolean availability,
                                      @RequestParam(required = false) String update

    )

     {
        if (update==null) {
            ProductDTO productDTO = productService.findById(id);
            return new ModelAndView("products/edit", "product", productDTO);
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCategory(String.valueOf(category));
        productDTO.setPrice(price);
        productDTO.setBarcode(barcode);
        productDTO.setQuantity(quantity);
        productDTO.setAvailability(availability);
        productDTO=productService.update(productDTO,id);
        return new ModelAndView("products/view", "product", productDTO);
    }


    @GetMapping("/add")
    public ModelAndView showAddForm() {
        return new ModelAndView("products/add", "product", new ProductDTO());
    }

    @PostMapping("/add")
    public ModelAndView addProduct(
            @RequestParam(value="name") String name,
            @RequestParam(value="description") String description,
            @RequestParam(value="category") Product.Category category,
            @RequestParam(value="price") Double price,
            @RequestParam(value="barcode") String barcode,
            @RequestParam(value="quantity") Integer quantity,
            @RequestParam(value="availability") Boolean availability,
            @RequestParam("image") MultipartFile image
    ) {
        String imagePath = productService.saveImage(image);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCategory(String.valueOf(category));
        productDTO.setPrice(price);
        productDTO.setBarcode(barcode);
        productDTO.setQuantity(quantity);
        productDTO.setAvailability(availability);
        productDTO.setImage(imagePath);

        productDTO = productService.save(productDTO);
        return new ModelAndView("products/view", "product", productDTO);
    }

    @RequestMapping("/delete/{id}")
    public ModelAndView  deleteProduct(@PathVariable("id") int id) {
        productService.delete(id);
        return new ModelAndView("redirect:/product");
    }

}