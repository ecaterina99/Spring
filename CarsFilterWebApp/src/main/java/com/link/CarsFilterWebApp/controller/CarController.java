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
        model.addAttribute("filterCriteria", new FilterCriteria());

        try {
            List<String> manufacturers = carService.getAllManufacturers();
            int[] yearRange = carService.getYearRange();
            model.addAttribute("manufacturers", manufacturers);
            model.addAttribute("minYear", yearRange[0]);
            model.addAttribute("maxYear", yearRange[1]);
        } catch (Exception e) {
            model.addAttribute("error", "Error loading data: " + e.getMessage());
        }
        return "index";
    }

    @PostMapping("/filter")
    public String filterCars(@ModelAttribute FilterCriteria criteria, Model model) {
        try {
            List<Car> filteredCars = carService.filterCars(criteria);
            model.addAttribute("cars", filteredCars);
            model.addAttribute("criteria", criteria);
            model.addAttribute("totalCount", filteredCars.size());

            addCommonAttributes(model, criteria);

        } catch (Exception e) {
            model.addAttribute("error", "Error filtering cars: " + e.getMessage());
            model.addAttribute("filterCriteria", criteria);
        }
        return "index";
    }

    private void addCommonAttributes(Model model, FilterCriteria criteria) {
        try {
            List<String> manufacturers = carService.getAllManufacturers();
            int[] yearRange = carService.getYearRange();
            model.addAttribute("manufacturers", manufacturers);
            model.addAttribute("minYear", yearRange[0]);
            model.addAttribute("maxYear", yearRange[1]);
            model.addAttribute("filterCriteria", criteria);
        } catch (Exception e) {
            model.addAttribute("error", "Error loading data: " + e.getMessage());
        }
    }
}

