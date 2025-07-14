package com.link.CarsFilterWebApp.service;

import com.link.CarsFilterWebApp.XMLParser.CarXMLParser;
import com.link.CarsFilterWebApp.model.Car;
import com.link.CarsFilterWebApp.model.FilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class CarService {

    @Autowired
    private CarXMLParser xmlParser;

    @Cacheable("cars")
    public List<Car> getAllCars() throws XMLStreamException, IOException {
        return xmlParser.parseXMLFile();
    }

    public List<Car> filterCars(FilterCriteria criteria) throws Exception {
        List<Car> allCars = getAllCars();
        return allCars.stream()
                .filter(car -> matchesFilter(car, criteria))
                .collect(Collectors.toList());
    }

    private boolean matchesFilter(Car car, FilterCriteria criteria) {

        //filter by Manufacturer
        if (criteria.hasManufacturerFilter()) {
            if (car.getManufacturer() == null ||
                    !car.getManufacturer().toLowerCase().contains(criteria.getManufacturer().toLowerCase())) {
                return false;
            }
        }

        //filter by year
        if (criteria.getMinYear() > 0 && car.getProductionYear() < criteria.getMinYear()) {
            return false;
        }
        if (criteria.getMaxYear() > 0 && car.getProductionYear() > criteria.getMaxYear()) {
            return false;
        }

        //filter by consumption type
        if (criteria.hasConsumptionFilter()) {
            if (car.getConsumption() == null || !car.getConsumption().equals(criteria.getConsumption())) {
                return false;
            }
            //check consumption range
            if (criteria.isCheckConsumptionRange() &&
                    (criteria.getConsumption() == Car.Consumption.FUEL ||
                            criteria.getConsumption() == Car.Consumption.HYBRID)) {

                double consumptionValue = car.getConsumptionValue();

                if (criteria.getMinConsumptionValue() > 0 && consumptionValue < criteria.getMinConsumptionValue()) {
                    return false;
                }
                if (criteria.getMaxConsumptionValue() > 0 && consumptionValue > criteria.getMaxConsumptionValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Cacheable("manufacturers")
    public List<String> getAllManufacturers() throws Exception {
        return getAllCars().stream()
                .map(Car::getManufacturer)   // extract manufacturer from each car
                .filter(Objects::nonNull)  // exclude null manufacturers
                .distinct() // remove duplicates
                .sorted() // sort alphabetically
                .collect(Collectors.toList()); // collect the result into a list
    }

    @Cacheable("yearRange")
    public int[] getYearRange() throws Exception {
        List<Car> cars = getAllCars();

        int minYear = cars.stream()
                .mapToInt(Car::getProductionYear)// extract year from each car
                .filter(year -> year > 0)  // exclude negative numbers
                .min() //min year from xml
                .orElse(2000); //default min year

        int maxYear = cars.stream()
                .mapToInt(Car::getProductionYear)// extract year from each car
                .filter(year -> year > 0)// exclude negative numbers
                .max()//max year from xml
                .orElse(2025); //default max year

        return new int[]{minYear, maxYear};
    }


    @Cacheable("consumptionRange")
    public double[] getConsumptionRange() throws Exception {
        List<Car> cars = getAllCars();

        double minConsumption = cars.stream()
                .mapToDouble(Car::getConsumptionValue)// extract Consumption from each car
                .filter(consumption -> consumption > 0)  // exclude negative numbers
                .min() //min Consumption from xml
                .orElse(0.0); //default min Consumption

        double maxConsumption = cars.stream()
                .mapToDouble(Car::getConsumptionValue)// extract Consumption from each car
                .filter(consumption -> consumption > 0)  // exclude negative numbers
                .max() //max Consumption from xml
                .orElse(50.0); //default max Consumption

        minConsumption = Math.floor(minConsumption);
        maxConsumption = Math.ceil(maxConsumption);

        return new double[]{minConsumption, maxConsumption};
    }

    @CacheEvict(value = {"cars", "manufacturers", "yearRange", "consumptionRange"}, allEntries = true)
    public void refreshCache() {
    }
}