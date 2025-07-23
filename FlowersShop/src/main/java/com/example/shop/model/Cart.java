package com.example.shop.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public  class Cart {
    private Integer productId;
    private Integer quantity;
    private String name;
}