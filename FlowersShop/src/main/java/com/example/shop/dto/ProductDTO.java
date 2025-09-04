package com.example.shop.dto;

import com.example.shop.model.Product;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonSetter;

@Setter
@Getter
public class ProductDTO {
    private Integer  id;
    private String name;
    private String description;
    private Product.Category category;
    private Double  price;
    private String barcode;
    private String image;
    private Integer quantity;
    private Boolean availability;

    @JsonSetter("category")
    public void setCategory(String categoryValue) {
        if (categoryValue == null || categoryValue.trim().isEmpty()) {
            this.category = null;
            return;
        }

        try {
            this.category = Product.Category.fromString(categoryValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + categoryValue +
                    ". Valid values are: bouquet, plant, gift");
        }
    }
}

