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

    private Double minConsumption;
    private Double maxConsumption;
    private boolean checkConsumptionRange;

    public FilterCriteria() {
        this.manufacturer = null;
        this.minYear = 0;
        this.maxYear = 0;
        this.consumption = null;
        this.minConsumption = null;
        this.maxConsumption = null;
        this.checkConsumptionRange = false;
    }

    // Helpers
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

    public double getMinConsumptionValue() {
        return minConsumption != null ? minConsumption : 0.0;
    }

    public double getMaxConsumptionValue() {
        return maxConsumption != null ? maxConsumption : 0.0;
    }

    public String getFilterDescription() {
        if (!isActive()) return "No filters applied";
        StringBuilder filterDescription = new StringBuilder();
        if (hasManufacturerFilter()) {
            filterDescription.append("Manufacturer: ").append(manufacturer).append(" ");
        }

        if (hasYearFilter()) {
            filterDescription.append("Year: ");
            if (minYear > 0 && maxYear > 0) {
                filterDescription.append(minYear).append("-").append(maxYear);
            } else {
                filterDescription.append("from ").append(minYear);
            }
            filterDescription.append(" ");
        }
        if (hasConsumptionFilter()) {
            filterDescription.append("Consumption: ").append(consumption);

            // show consumption range for FUEL и HYBRID
            if (checkConsumptionRange &&
                    (consumption == Car.Consumption.FUEL || consumption == Car.Consumption.HYBRID)) {
                filterDescription.append(" (");
                if (getMinConsumptionValue() > 0 && getMaxConsumptionValue() > 0) {
                    filterDescription.append(getMinConsumptionValue()).append("-").append(getMaxConsumptionValue());
                } else if (getMinConsumptionValue() > 0) {
                    filterDescription.append("from ").append(getMinConsumptionValue());
                } else if (getMaxConsumptionValue() > 0) {
                    filterDescription.append("to ").append(getMaxConsumptionValue());
                }
                filterDescription.append(")");
            }
        }
        return filterDescription.toString().trim();
    }
}