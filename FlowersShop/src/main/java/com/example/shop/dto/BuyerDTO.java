package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerDTO {
    private int id;
    private String fullName;
    private String city;
    private String address;
    private String phone;
    private String email;
    private String postalCode;
}
