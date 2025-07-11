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

    public int getMaxYear() {
        return maxYear;
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

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public void setConsumption(Car.Consumption consumption) {
        this.consumption = consumption;
    }

    public void setMinConsumption(double minConsumption) {
        this.minConsumption = minConsumption;
    }

    public void setMaxConsumption(double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public void setCheckConsumptionRange(boolean checkConsumptionRange) {
        this.checkConsumptionRange = checkConsumptionRange;
    }

// Helper methods
    public boolean hasManufacturerFilter() {
        return manufacturer != null && !manufacturer.trim().isEmpty();
    }

    public boolean hasYearFilter() {
        return minYear > 0 || maxYear > 0;
    }

    public boolean hasConsumptionFilter() {
        return consumption != null;
    }


    public boolean isActive() {
        return hasManufacturerFilter() || hasYearFilter() || hasConsumptionFilter();
    }

    public String getFilterDescription() {
        if (!isActive()) return "No filters applied";

        StringBuilder desc = new StringBuilder();

        if (hasManufacturerFilter()) {
            desc.append("Manufacturer: ").append(manufacturer).append(" ");
        }

        if (hasYearFilter()) {
            desc.append("Year: ");
            if (minYear > 0 && maxYear > 0) {
                desc.append(minYear).append("-").append(maxYear);
            } else if (minYear > 0) {
                desc.append("from ").append(minYear);
            } else if (maxYear > 0) {
                desc.append("up to ").append(maxYear);
            }
            desc.append(" ");
        }

        if (hasConsumptionFilter()) {
            desc.append("Consumption: ").append(consumption);
            if (checkConsumptionRange) {
                desc.append(" (").append(minConsumption).append("-").append(maxConsumption).append(")");
            }
        }

        return desc.toString().trim();
    }

  /*  public void reset() {
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

   */

}