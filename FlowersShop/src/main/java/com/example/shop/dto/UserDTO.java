package com.example.shop.dto;

import com.example.shop.model.Sale;
import com.example.shop.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private int id;
    private String fullName;
    private String city;
    private String address;
    private String phone;
    private String email;
    private String postalCode;
    private List<Sale> sales;
    private String passwordHash;
    private User.Role role;
}
