package com.link.CarsFilterWebApp.service;

import com.link.CarsFilterWebApp.XMLParser.CarXMLParser;
import com.link.CarsFilterWebApp.model.Car;
import com.link.CarsFilterWebApp.model.FilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarXMLParser xmlParser;

    private List<Car> cachedCars = null;

    public List<Car> getAllCars() throws Exception {
        if (cachedCars == null) {
            cachedCars = xmlParser.parseXMLFile();
        }
        return cachedCars;
    }

    public List<Car> filterCars(FilterCriteria criteria) throws Exception {
        List<Car> allCars = getAllCars();
        return allCars.stream()
                .filter(car -> matchesFilter(car, criteria))
                .collect(Collectors.toList());
    }


    private boolean matchesFilter(Car car, FilterCriteria criteria) {
        if (criteria.getManufacturer() != null && !criteria.getManufacturer().trim().isEmpty()) {
            if (car.getManufacturer() == null ||
                    !car.getManufacturer().toLowerCase().contains(criteria.getManufacturer().toLowerCase())) {
                return false;
            }
        }

        if (criteria.getMinYear() > 0) {
            if (car.getProductionYear() < criteria.getMinYear()) {
                return false;
            }
        }

        if (criteria.getMaxYear() > 0) {
            if (car.getProductionYear() > criteria.getMaxYear()) {
                return false;
            }
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
        return true;
    }

    public List<String> getAllManufacturers() throws Exception {
        return getAllCars().stream()
                .map(Car::getManufacturer)   // extract manufacturer from each car
                .filter(manufacturer -> manufacturer != null)  // exclude null manufacturers
                .distinct() // remove duplicates
                .sorted() // sort alphabetically
                .collect(Collectors.toList()); // collect the result into a list
    }

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

    public void refreshCache() {
        cachedCars = null;
    }

    public List<Car> filterByManufacturer(String manufacturer) throws Exception {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setManufacturer(manufacturer);
        return filterCars(criteria);
    }

    public List<Car> filterByYear( int minYear, int maxYear) throws Exception {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setMinYear(minYear);
        criteria.setMaxYear(maxYear);
        return filterCars(criteria);
    }

    public List<Car> filterByConsumption(Car.Consumption consumption) throws Exception {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setConsumption(consumption);
        return filterCars(criteria);
    }

    public List<Car> filterByConsumptionWithRange(Car.Consumption consumption,
                                                  double minConsumption,
                                                  double maxConsumption) throws Exception {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setConsumption(consumption);
        criteria.setMinConsumption(minConsumption);
        criteria.setMaxConsumption(maxConsumption);
        criteria.setCheckConsumptionRange(true);
        return filterCars(criteria);
    }

}