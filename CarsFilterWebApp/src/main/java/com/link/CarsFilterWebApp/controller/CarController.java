package com.link.CarsFilterWebApp.controller;

import com.link.CarsFilterWebApp.model.Car;
import com.link.CarsFilterWebApp.model.FilterCriteria;
import com.link.CarsFilterWebApp.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            // Reload data for form
            List<String> manufacturers = carService.getAllManufacturers();
            int[] yearRange = carService.getYearRange();

            model.addAttribute("manufacturers", manufacturers);
            model.addAttribute("minYear", yearRange[0]);
            model.addAttribute("maxYear", yearRange[1]);
            model.addAttribute("filterCriteria", criteria);

        } catch (Exception e) {
            model.addAttribute("error", "Error filtering cars: " + e.getMessage());
            model.addAttribute("filterCriteria", criteria);
        }
        // return templates/index.html
        return "index";
    }

    //REST CONTROLLER

    @PostMapping("/api/filter")
    @ResponseBody
    public ResponseEntity<List<Car>> filterCarsApi(@RequestBody FilterCriteria criteria) {
        try {
            List<Car> cars = carService.filterCars(criteria);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/manufacturers")
    @ResponseBody
    public ResponseEntity<List<String>> getManufacturers() {
        try {
            List<String> manufacturers = carService.getAllManufacturers();
            return ResponseEntity.ok(manufacturers);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/year-range")
    @ResponseBody
    public ResponseEntity<int[]> getYearRange() {
        try {
            int[] yearRange = carService.getYearRange();
            return ResponseEntity.ok(yearRange);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

