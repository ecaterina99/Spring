package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllUsersDto {
    List<UserDTO> users;
    long count;
}
