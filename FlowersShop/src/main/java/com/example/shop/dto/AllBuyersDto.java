package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllBuyersDto {
    List<BuyerDTO> buyers;
    long count;
}
