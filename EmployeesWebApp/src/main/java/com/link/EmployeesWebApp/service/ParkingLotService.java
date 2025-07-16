package com.link.EmployeesWebApp.service;

import com.link.EmployeesWebApp.model.ParkingLot;
import com.link.EmployeesWebApp.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> findAll() {
        return parkingLotRepository.allLotsWithStatus();
    }

}