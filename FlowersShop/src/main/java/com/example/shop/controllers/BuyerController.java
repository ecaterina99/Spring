package com.example.shop.controllers;

import com.example.shop.dto.*;
import com.example.shop.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/")
    public ResponseEntity<Object> addBuyer(@RequestBody BuyerDTO buyerDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        BuyerDTO savedBuyerDTO = buyerService.save(buyerDTO);
        return ResponseEntity.ok(savedBuyerDTO != null ? savedBuyerDTO : new HashMap<>());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBuyer(@PathVariable("id") int id, @RequestBody BuyerDTO buyerDTO) {
        if(buyerDTO.getId() != 0) {
            buyerDTO.setId(0);
        }
        BuyerDTO buyerDTOResponse = buyerService.update(buyerDTO,id);
        if(buyerDTOResponse == null) {
            return ResponseEntity.ok(new HashMap<>());
        }
        return ResponseEntity.ok(buyerDTOResponse);
    }

    // Delete buyer: DELETE /buyer/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBuyer(@PathVariable("id") int id)
           {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }
        boolean wasDeleted = buyerService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.ok(new HashMap<>());
        }
        return ResponseEntity.badRequest().build();
    }
}
