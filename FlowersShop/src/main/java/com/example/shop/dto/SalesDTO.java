package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesDTO {
    private int buyerId;
    private int productId;
    private int quantity;
}