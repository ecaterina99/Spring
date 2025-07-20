package com.example.shop.service;

import com.example.shop.dto.BuyerDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.Buyer;
import com.example.shop.model.Sale;
import com.example.shop.repository.BuyersRepositoryCrud;
import com.example.shop.repository.BuyersRepositoryJpa;
import com.example.shop.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class  BuyerService {

    @Autowired
    BuyersRepositoryCrud buyersRepositoryCrud;
    @Autowired
    BuyersRepositoryJpa buyersRepositoryJpa;
    @Autowired
    private DTOManager dtoManager;
    @Autowired
    private SaleRepository saleRepository;

    private BuyerDTO buyerToDto(Buyer buyer) {
        return dtoManager.buyerToDto(buyer);
    }

    private Buyer buyerDtoToModel(BuyerDTO buyerDto) {
   Buyer buyer = new Buyer();
        String[] nameParts = buyerDto.getFullName().split(" ");
        buyer.setFirstName(nameParts[0]);
        buyer.setLastName(nameParts[1]);
        buyer.setEmail(buyerDto.getEmail());
        buyer.setPhone(buyerDto.getPhone());
        buyer.setAddress(buyerDto.getAddress());
        buyer.setCity(buyerDto.getCity());
        buyer.setPostalCode(buyerDto.getPostalCode());
        buyer.setPasswordHash(buyerDto.getPasswordHash());

        List<Sale> sales = buyerDto.getSales();
        if (sales != null && !sales.isEmpty()) {
            List<Sale>salesList = saleRepository.findAll();
            buyer.setSales(salesList);
        }

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

    public BuyerDTO findByEmail(String email) {
        Optional<Buyer> buyerOptional = buyersRepositoryCrud.findByEmail(email);
        Buyer buyer = buyerOptional.orElse(null);
        return buyerToDto(buyer);
    }


    public BuyerDTO save(BuyerDTO buyerDTO) {
        Buyer buyer = buyerDtoToModel(buyerDTO);
        buyersRepositoryCrud.save(buyer);
        return buyerToDto(buyer);
    }

    public Buyer register(Buyer buyer) {
        return  buyersRepositoryCrud.save(buyer);
    }


    public BuyerDTO update(BuyerDTO buyerDTO, int id) {
        Buyer buyer = buyerDtoToModel(buyerDTO);
        buyer.setId(id);
        Buyer bayerDB = buyersRepositoryCrud.findById(id).orElse(null);
        if (bayerDB == null) {
            return null;
        }
        if(buyer.getFirstName()!=null) {
            bayerDB.setFirstName(buyer.getFirstName());
        }
        if(buyer.getLastName()!=null) {
            bayerDB.setLastName(buyer.getLastName());
        }
        if(buyer.getEmail()!=null) {
            bayerDB.setEmail(buyer.getEmail());
        }
        if(buyer.getPhone()!=null) {
            bayerDB.setPhone(buyer.getPhone());
        }
        if(buyer.getAddress()!=null) {
            bayerDB.setAddress(buyer.getAddress());
        }
        if(buyer.getCity()!=null) {
            bayerDB.setCity(buyer.getCity());
        }
        if(buyer.getPostalCode()!=null) {
            bayerDB.setPostalCode(buyer.getPostalCode());
        }
       buyer=buyersRepositoryCrud.save(bayerDB);
        return buyerToDto(buyer);
        }

    public boolean delete(int id) {
        buyersRepositoryCrud.deleteById(id);
        Optional<Buyer> buyerOptional = buyersRepositoryCrud.findById(id);
        return buyerOptional.isEmpty();
    }

    public List<BuyerDTO> findAllBuyerAndSales() {
        List<BuyerDTO> buyerDTOList = new ArrayList<>();
        List<Buyer> buyers = buyersRepositoryJpa.findAllBuyersAndSales();
        for (Buyer buyer : buyers) {
            BuyerDTO buyerDTO = buyerToDto(buyer);
            buyerDTOList.add(buyerDTO);
        }
        return buyerDTOList;
    }



}



