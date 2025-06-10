package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AllProductsDto {
    List<ProductDTO> products;
    long count;
}
