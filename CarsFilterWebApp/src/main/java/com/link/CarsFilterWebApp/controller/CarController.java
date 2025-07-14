package com.link.CarsFilterWebApp.controller;

import com.link.CarsFilterWebApp.model.Car;
import com.link.CarsFilterWebApp.model.FilterCriteria;
import com.link.CarsFilterWebApp.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")

public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Car Filter Application");
        FilterCriteria criteria = new FilterCriteria();
        model.addAttribute("filterCriteria", criteria);
        addCommonAttributes(model, criteria);
        return "index";
    }

    @PostMapping("/filter")
    public String filterCars(@ModelAttribute FilterCriteria criteria, Model model
    ) {
        try {
            List<Car> filteredCars = carService.filterCars(criteria);
            model.addAttribute("cars", filteredCars);
            model.addAttribute("totalCount", filteredCars.size());

        } catch (Exception e) {
            model.addAttribute("error", "Error filtering cars: " + e.getMessage());
        }
        model.addAttribute("filterCriteria", criteria);
        addCommonAttributes(model, criteria);
        return "index";
    }

    private void addCommonAttributes(Model model, FilterCriteria criteria) {
        try {
            model.addAttribute("filterCriteria", criteria);

            List<String> manufacturers = carService.getAllManufacturers();
            model.addAttribute("manufacturers", manufacturers);

            int[] yearRange = carService.getYearRange();
            model.addAttribute("minYear", yearRange[0]);
            model.addAttribute("maxYear", yearRange[1]);

            double[] consumptionRange = carService.getConsumptionRange();
            model.addAttribute("minConsumption", consumptionRange[0]);
            model.addAttribute("maxConsumption", consumptionRange[1]);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading data: " + e.getMessage());
        }
    }
}

