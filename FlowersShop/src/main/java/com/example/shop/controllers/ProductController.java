package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.UserAttributes;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserAttributes userAttributes;

    @Autowired
    public ProductController(UserAttributes userAttributes, ProductService productService) {
        this.userAttributes = userAttributes;
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        List<ProductDTO> productsDTO = productService.findAll();
        model.addAttribute("allProducts", productsDTO);
        return "productsManager/products";
    }

    @GetMapping("/bouquets")
    public String bouquets(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("bouquet"));
        } else if(sort.equalsIgnoreCase("priceDesc")) {
            products=productService.orderByPriceDesc(Product.Category.valueOf("bouquet"));
        }else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "bouquets";
    }
    @GetMapping("/plants")
    public String plants (Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("plant"));
        } else if(sort.equalsIgnoreCase("priceDesc")) {
            products=productService.orderByPriceDesc(Product.Category.valueOf("plant"));
        }else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "plants";
    }
    @GetMapping("/gifts")
    public String gifts(Model model, Authentication authentication, @RequestParam(name = "sort", defaultValue = "relevance") String sort) {
        List<ProductDTO> products;
        if (sort.equalsIgnoreCase("price")) {
            products = productService.orderByPriceAsc(Product.Category.valueOf("gift"));
        } else if(sort.equalsIgnoreCase("priceDesc")) {
            products=productService.orderByPriceDesc(Product.Category.valueOf("gift"));
        }else {
            products = productService.findAll();
        }
        userAttributes.addUserAttributes(model, authentication);
        model.addAttribute("allProducts", products);
        return "gifts";
    }
    @GetMapping("/info/{id}")
    public String showProduct(@PathVariable("id") int id, Model model, Authentication authentication) {
        userAttributes.addUserAttributes(model, authentication);
        ProductDTO productDTO = productService.findById(id);
        List<ProductDTO> randomGifts = productService.getRandomProducts();

        model.addAttribute("product", productDTO);
        model.addAttribute("allProducts", randomGifts);
        return "info";
    }

}


/*package com.example.shop.controllers;

import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.ViewUtils;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/")
    public ModelAndView listProducts(@CookieValue(name = "authenticated", defaultValue = "no") String auth,
                                     @CookieValue(name = "email", defaultValue = "guest") String email,
                                     @CookieValue(name = "role", defaultValue = "buyer") String role) {
        List<ProductDTO> productsDTO = productService.findAll();
        ModelAndView modelAndView = new ModelAndView("productsManager/products", "allProducts", productsDTO);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView showProduct(@PathVariable("id") int id,
                                    @CookieValue(name = "authenticated", defaultValue = "no") String auth,
                                    @CookieValue(name = "email", defaultValue = "guest") String email,
                                    @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        ProductDTO productDTO = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("productsManager/view", "product", productDTO);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") int id,
                                     @CookieValue(name = "authenticated", defaultValue = "no") String auth,
                                     @CookieValue(name = "email", defaultValue = "guest") String email,
                                     @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        ProductDTO productDTO = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("productsManager/edit", "product", productDTO);
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateProduct(@PathVariable("id") int id,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "description", required = false) String description,
                                      @RequestParam(value = "category", required = false) Product.Category category,
                                      @RequestParam(value = "price", required = false) Double price,
                                      @RequestParam(value = "barcode", required = false) String barcode,
                                      @RequestParam(value = "quantity", required = false) Integer quantity,
                                      @RequestParam(value = "availability", required = false) Boolean availability,
                                      @RequestParam(value = "image", required = false) MultipartFile image){
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
        return new ModelAndView("productsManager/view", "product", productDTO);
    }

    @GetMapping("/add")
    public ModelAndView showAddForm(
            @CookieValue(name = "authenticated", defaultValue = "no") String auth,
            @CookieValue(name = "email", defaultValue = "guest") String email,
            @CookieValue(name = "role", defaultValue = "buyer") String role
    ) {
        ModelAndView modelAndView = new ModelAndView("productsManager/add", "product", new ProductDTO());
        viewUtils.addAuthenticationData(modelAndView, auth, email, role);
        return modelAndView;
    }

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
        return new ModelAndView("redirect:/product/", "product", productDTO);
    }

    @RequestMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable("id") int id
    ) {
        productService.delete(id);
        return new ModelAndView("redirect:/product/");
    }
}


 */