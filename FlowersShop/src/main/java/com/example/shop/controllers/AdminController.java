package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.Product;
import com.example.shop.model.Sale;
import com.example.shop.service.ProductService;
import com.example.shop.service.SaleService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserAttributes userAttributes;
    private final ProductService productService;
    private final SaleService saleService;
    private final UserService userService;

    @Autowired
    public AdminController(UserAttributes userAttributes, ProductService productService, SaleService saleService, UserService userService) {
        this.userAttributes = userAttributes;
        this.productService = productService;
        this.saleService = saleService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String home(Model model, Authentication authentication) {
        List<ProductDTO> allProducts = productService.findAll();
        List<Sale> allSales = saleService.findAll();
        List<UserDTO> allUsers = userService.findAllUsers();

        model.addAttribute("allProducts", allProducts);
        model.addAttribute("allSales", allSales);
        model.addAttribute("allUsers", allUsers);
        userAttributes.addUserAttributes(model, authentication);

        return "dashboard";
    }

    @GetMapping("/products")
    public String listProducts(Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        List<ProductDTO> productsDTO = productService.findAll();
        model.addAttribute("allProducts", productsDTO);
        return "productsManager/products";
    }

    @GetMapping("view/{id}")
    public String listProducts(@PathVariable("id") int id, Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);
        return "productsManager/view";
    }


    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);
        return "productsManager/edit";
    }

    @PostMapping("edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model, Authentication authentication,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "category", required = false) Product.Category category,
                              @RequestParam(value = "price", required = false) Double price,
                              @RequestParam(value = "barcode", required = false) String barcode,
                              @RequestParam(value = "quantity", required = false) Integer quantity,
                              @RequestParam(value = "availability", required = false) Boolean availability,
                              @RequestParam(value = "image", required = false) MultipartFile image) {

        String imagePath = productService.saveImage(image);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCategory(String.valueOf(category));
        productDTO.setPrice(price);
        productDTO.setBarcode(barcode);
        productDTO.setQuantity(quantity);
        productDTO.setAvailability(availability);
        productDTO.setImage(imagePath);

        productDTO = productService.update(productDTO, id);
        model.addAttribute("product", productDTO);
        userAttributes.addUserAttributes(model, authentication);

        return "productsManager/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, Authentication authentication) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("product", productDTO);
        userAttributes.addUserAttributes(model, authentication);
        return "productsManager/add";
    }

    @PostMapping("/add")
    public String addProduct(Model model, Authentication authentication,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "description") String description,
                             @RequestParam(value = "category") Product.Category category,
                             @RequestParam(value = "price") Double price,
                             @RequestParam(value = "barcode") String barcode,
                             @RequestParam(value = "quantity") Integer quantity,
                             @RequestParam(value = "availability") Boolean availability,
                             @RequestParam("image") MultipartFile image) {

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
        model.addAttribute("product", productDTO);
        userAttributes.addUserAttributes(model, authentication);

        return "productsManager/view";
    }

    @RequestMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model, Authentication authentication) {
        productService.delete(id);
        userAttributes.addUserAttributes(model, authentication);
        return "redirect:/admin/products";
    }


}




