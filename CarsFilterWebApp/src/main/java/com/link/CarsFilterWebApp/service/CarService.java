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
        if (criteria.getMinYear() > 0 && car.getProductionYear() < criteria.getMinYear()) {
            return false;
        }
        if (criteria.getMaxYear() > 0 && car.getProductionYear() > criteria.getMaxYear()) {
            return false;
        }
        if (criteria.getConsumption() != null) {
            if (car.getConsumption() == null || !car.getConsumption().equals(criteria.getConsumption())) {
                return false;
            }
            if (criteria.isCheckConsumptionRange()) {
                double consumptionValue = car.getConsumptionValue();
                if (consumptionValue < criteria.getMinConsumption() ||
                        consumptionValue > criteria.getMaxConsumption()) {
                    return false;
                }
            }
        }
        if (criteria.getManufacturer() != null && !criteria.getManufacturer().trim().isEmpty()) {
            if (car.getManufacturer() == null ||
                    !car.getManufacturer().toLowerCase().contains(criteria.getManufacturer().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @Cacheable("manufacturers")
    public List<String> getAllManufacturers() throws Exception {
        return getAllCars().stream()
                .map(Car::getManufacturer)   // extract manufacturer from each car
                .filter(manufacturer -> manufacturer != null)  // exclude null manufacturers
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
                .min()
                .orElse(1900); //min year

        int maxYear = cars.stream()
                .mapToInt(Car::getProductionYear)// extract year from each car
                .filter(year -> year > 0)// exclude negative numbers
                .max()
                .orElse(2025); //max year

        return new int[]{minYear, maxYear};
    }

    @CacheEvict(value = {"cars", "manufacturers", "yearRange"}, allEntries = true)
    public void refreshCache() {
    }
}