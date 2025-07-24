package com.example.shop.controllers;


import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.ViewUtils;
import com.example.shop.model.Product;
import com.example.shop.service.AuthorizationService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ViewUtils viewUtils;

    public ProductController(ProductService productService, ViewUtils viewUtils) {
        this.productService = productService;
        this.viewUtils = viewUtils;
    }
// Displays list of all products
    @GetMapping("/")
    public ModelAndView listProducts() {
        List<ProductDTO> productsDTO = productService.findAll();
        return new ModelAndView("products/products","allProducts",productsDTO);
    }
//Displays detailed view of a specific product
    @GetMapping("/{id}")
    public ModelAndView showProduct(
            @PathVariable("id") int id
    ) {
        ProductDTO productDTO = productService.findById(id);
        return new ModelAndView("products/view", "product", productDTO);
    }
//Displays product edit form for existing product
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable int id) {
        ProductDTO productDTO = productService.findById(id);
        return new ModelAndView("products/edit", "product", productDTO);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateProduct(
            @PathVariable("id") int id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) Product.Category category,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "barcode", required = false) String barcode,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "availability", required = false) Boolean availability
    ) {
        try {
            ProductDTO productDTO = buildProductUpdateDTO(id, name, description, category,
                    price, barcode, quantity, availability);

            ProductDTO updatedProduct = productService.update(productDTO, id);
            if (updatedProduct == null) {
                return new ModelAndView("error/404", "message", "Product not found");
            }

            return new ModelAndView("products/view", "product", updatedProduct);
        } catch (Exception e) {
            return new ModelAndView("error/500", "message", "Failed to update product: " + e.getMessage());
        }
    }

    @GetMapping("/add")
    public ModelAndView showAddForm() {
        return new ModelAndView("products/add", "product", new ProductDTO());
    }
    //Displays form for adding new product
    @PostMapping("/add")
    public ModelAndView addProduct(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "category") Product.Category category,
            @RequestParam(value = "price") Double price,
            @RequestParam(value = "barcode") String barcode,
            @RequestParam(value = "quantity") Integer quantity,
            @RequestParam(value = "availability") Boolean availability,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                return new ModelAndView("products/add", "error", "Product name is required");
            }
            if (price == null || price <= 0) {
                return new ModelAndView("products/add", "error", "Valid price is required");
            }
            String imagePath = productService.saveImage(image);
            ProductDTO productDTO = buildNewProductDTO(name, description, category,
                    price, barcode, quantity, availability, imagePath);
            ProductDTO savedProduct = productService.save(productDTO);
            return new ModelAndView("products/view", "product", savedProduct);
        } catch (Exception e) {
            return new ModelAndView("products/add", "error", "Failed to create product: " + e.getMessage());
        }
    }
    //Deletes a product by ID
    @RequestMapping("/delete/{id}")
    public ModelAndView  deleteProduct(@PathVariable("id") int id) {
        productService.delete(id);
        return new ModelAndView("redirect:/product/");
    }
//Displays filtered list of bouquet products with authentication context
@GetMapping("/bouquets")
public ModelAndView listBouquets(
        @CookieValue(name = "authenticated", defaultValue = "no") String auth,
        @CookieValue(name = "email", defaultValue = "guest") String email,
        @CookieValue(name = "role", defaultValue = "buyer") String role
) {
    List<ProductDTO> allProducts = productService.findAll();
    List<ProductDTO> bouquets = filterProductsByCategory(allProducts, "bouquet");

    ModelAndView modelAndView = new ModelAndView("bouquets");
    modelAndView.addObject("allProducts", bouquets);
    viewUtils.addAuthenticationData(modelAndView, auth, email, role);

    return modelAndView;
}

    @GetMapping("/info/{id}")
    public ModelAndView showProductInfo(
            @PathVariable("id") int id
    ) {
        ProductDTO productDTO = productService.findById(id);
        return new ModelAndView("info", "product", productDTO);
    }

    //Builds ProductDTO for update operation
    private ProductDTO buildProductUpdateDTO(int id, String name, String description,
                                             Product.Category category, Double price, String barcode,
                                             Integer quantity, Boolean availability) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCategory(category != null ? String.valueOf(category) : null);
        productDTO.setPrice(price != null ? price : 0);
        productDTO.setBarcode(barcode);
        productDTO.setQuantity(quantity != null ? quantity : 0);
        productDTO.setAvailability(availability != null ? availability : false);
        return productDTO;
    }
    //Builds ProductDTO for new product creation
    private ProductDTO buildNewProductDTO(String name, String description, Product.Category category,
                                          Double price, String barcode, Integer quantity,
                                          Boolean availability, String imagePath) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCategory(String.valueOf(category));
        productDTO.setPrice(price);
        productDTO.setBarcode(barcode);
        productDTO.setQuantity(quantity);
        productDTO.setAvailability(availability);
        productDTO.setImage(imagePath);
        return productDTO;
    }
// Filters products by category with null safety
    private List<ProductDTO> filterProductsByCategory(List<ProductDTO> products, String categoryName) {
        return products.stream()
                .filter(product -> product.getCategory() != null)
                .filter(product -> product.getCategory().toString().equalsIgnoreCase(categoryName))
                .toList();
    }

}