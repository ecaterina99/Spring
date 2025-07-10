package com.link.CarsFilterWebApp.model;

public class FilterCriteria {
    private String manufacturer;

    private int minYear;
    private int maxYear;

    private Car.Consumption consumption;

    private double  minConsumption;
    private double  maxConsumption;
    private boolean checkConsumptionRange;

    private String filterType;


    public FilterCriteria() {
        this.manufacturer = null;
        this.minYear = 0;
        this.maxYear = 0;
        this.consumption = null;
        this.minConsumption = 0.0;
        this.maxConsumption = 0.0;
        this.checkConsumptionRange = false;
        this.filterType = "all";
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getMinYear() {
        return minYear;
    }

    public Car.Consumption getConsumption() {
        return consumption;
    }

    public double getMinConsumption() {
        return minConsumption;
    }

    public double getMaxConsumption() {
        return maxConsumption;
    }

    public boolean isCheckConsumptionRange() {
        return checkConsumptionRange;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public void setConsumption(Car.Consumption consumption) {
        this.consumption = consumption;
    }

    public void setMinConsumption(int minConsumption) {
        this.minConsumption = minConsumption;
    }

    public void setMaxConsumption(int maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public void setCheckConsumptionRange(boolean checkConsumptionRange) {
        this.checkConsumptionRange = checkConsumptionRange;
    }

    public boolean hasActiveFilters() {
        return (manufacturer != null && !manufacturer.trim().isEmpty()) ||
                minYear > 0 || maxYear > 0 ||
                consumption != null ||
                checkConsumptionRange;
    }
    public void reset() {
        this.manufacturer = null;
        this.minYear = 0;
        this.maxYear = 0;
        this.consumption = null;
        this.minConsumption = 0.0;
        this.maxConsumption = 0.0;
        this.checkConsumptionRange = false;
        this.filterType = "all";
    }

    @Override
    public String toString() {
        return "FilterCriteria{" +
                "manufacturer='" + manufacturer + '\'' +
                ", minYear=" + minYear +
                ", maxYear=" + maxYear +
                ", consumption=" + consumption +
                ", minConsumption=" + minConsumption +
                ", maxConsumption=" + maxConsumption +
                ", checkConsumptionRange=" + checkConsumptionRange +
                ", filterType='" + filterType + '\'' +
                '}';
    }

}