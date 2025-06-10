package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private double price;
    private String image;
    private boolean availability;
}
