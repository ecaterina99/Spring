package com.link.CarsFilterWebApp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilterCriteria {
    private String manufacturer;
    private int minYear;
    private int maxYear;

    private Car.Consumption consumption;

    private double minConsumption;
    private double maxConsumption;
    private boolean checkConsumptionRange;

    public FilterCriteria() {
        this.manufacturer = null;
        this.minYear = 0;
        this.maxYear = 0;
        this.consumption = null;
        this.minConsumption = 0.0;
        this.maxConsumption = 0.0;
        this.checkConsumptionRange = false;
    }

    // Helper methods
    public boolean hasManufacturerFilter() {
        return manufacturer != null && !manufacturer.trim().isEmpty();
    }

    public boolean hasYearFilter() {
        return true;
    }

    public boolean hasConsumptionFilter() {
        return consumption != null;
    }

    public boolean isActive() {
        return hasManufacturerFilter() || hasYearFilter() || hasConsumptionFilter();
    }

    public String getFilterDescription() {
        if (!isActive()) return "No filters applied";
        StringBuilder filterDescription = new StringBuilder();
        if (hasManufacturerFilter()) {
            filterDescription.append("Manufacturer: ").append(manufacturer).append(" ");
        }
        if (hasYearFilter()) {
            filterDescription.append("Year: ");
            if (maxYear > 0) {
                filterDescription.append(minYear).append("-").append(maxYear);
            } else {
                filterDescription.append("from ").append(minYear);
            }
            filterDescription.append(" ");
        }
        if (hasConsumptionFilter()) {
            filterDescription.append("Consumption: ").append(consumption);
            if (checkConsumptionRange) {
                filterDescription.append(" (").append(minConsumption).append("-").append(maxConsumption).append(")");
            }
        }
        return filterDescription.toString().trim();
    }
}