package com.example.shop.service;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.dto.ProductDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.Buyer;
import com.example.shop.model.Product;
import com.example.shop.repository.BuyersRepositoryCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {

    @Autowired
    BuyersRepositoryCrud buyersRepositoryCrud;
    @Autowired
    private DTOManager dtoManager;

    private BuyerDTO buyerToDto(Buyer buyer) {
        return dtoManager.buyerToDto(buyer);
    }

    private Buyer buyerDtoToBuyer(BuyerDTO buyerDto) {
   Buyer buyer = new Buyer();
        String[] nameParts = buyerDto.getFullName().split(" ");
        buyer.setFirstName(nameParts[0]);
        buyer.setLastName(nameParts[1]);
        buyer.setEmail(buyerDto.getEmail());
        buyer.setPhone(buyerDto.getPhone());
        buyer.setAddress(buyerDto.getAddress());
        buyer.setCity(buyerDto.getCity());
        return buyer;
    }

    public List<BuyerDTO> findAllBuyers() {
        List<BuyerDTO> buyerDTOList = new ArrayList<>();
        Iterable<Buyer> iterableBuyers = buyersRepositoryCrud.findAll();
        for (Buyer buyer : iterableBuyers) {
            BuyerDTO buyerDTO = buyerToDto(buyer);
            buyerDTOList.add(buyerDTO);
        }
        return buyerDTOList;
    }

    public BuyerDTO findById(int id) {
        Optional<Buyer> buyerOptional = buyersRepositoryCrud.findById(id);
        Buyer buyer = buyerOptional.orElse(null);
        return buyerToDto(buyer);
    }

}


