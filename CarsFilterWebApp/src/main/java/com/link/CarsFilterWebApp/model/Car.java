package com.link.CarsFilterWebApp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Car {
    private String id;
    private String manufacturer;
    private String model;
    private int productionYear;
    private double horsepower;
    private double price;
    private double consumptionValue;
    private Consumption consumption;

    public enum Consumption {
        ELECTRIC, FUEL, HYBRID
    }

    public Car() {
        reset();
    }

    public void reset() {
        this.id = null;
        this.manufacturer = null;
        this.model = null;
        this.productionYear = 0;
        this.horsepower = 0.0;
        this.price = 0.0;
        this.consumptionValue = 0.0;
        this.consumption = null;
    }
}