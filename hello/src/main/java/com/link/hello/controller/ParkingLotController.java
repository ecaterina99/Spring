package com.link.hello.controller;

import com.link.hello.model.ParkingLot;
import com.link.hello.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/park")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping("/")
    public List<ParkingLot> parkingLots() {
        return parkingLotService.findAll();
    }

}