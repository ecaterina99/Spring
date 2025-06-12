package com.example.shop.controllers;

import com.example.shop.dto.*;
import com.example.shop.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;


    // Show all buyers: GET /product/all
    @GetMapping("/all")
    public AllBuyersDto showAllBuyers() {
        AllBuyersDto allBuyersDto = new AllBuyersDto();
        List<BuyerDTO> buyersDTO = buyerService.findAllBuyers();
        allBuyersDto.setBuyers(buyersDTO);
        return allBuyersDto;
    }

   @GetMapping("/{id}")
   public ResponseDTO showBuyerById(@PathVariable("id") int id) {
        ResponseDTO responseDTO = new ResponseDTO();
        BuyerDTO buyerDTO = buyerService.findById(id);
        responseDTO.setData(buyerDTO);
        responseDTO.setSuccess(buyerDTO !=null);
        return responseDTO;
    }
}
