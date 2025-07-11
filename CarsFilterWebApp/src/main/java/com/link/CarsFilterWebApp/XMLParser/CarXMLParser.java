package com.link.CarsFilterWebApp.XMLParser;

import com.link.CarsFilterWebApp.model.Car;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarXMLParser {
    private static final String XML_FILE = "cars.xml";

    public List<Car> parseXMLFile() throws IOException, XMLStreamException {
        List<Car> cars = new ArrayList<>();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();

        ClassPathResource resource = new ClassPathResource(XML_FILE);


        try (InputStream inputStream = resource.getInputStream();
             Reader reader = new FileReader(XML_FILE)) {

            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(reader);

            Car currentCar = new Car();
            String currentElement = null;

            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();

                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        currentElement = xmlStreamReader.getLocalName();

                        if ("car".equals(currentElement)) {
                            currentCar.reset();
                            currentCar.setId(xmlStreamReader.getAttributeValue(null, "id"));
                        } else if ("consumption".equals(currentElement)) {
                            String type = xmlStreamReader.getAttributeValue(null, "type");
                            if (type != null) {
                                try {
                                    currentCar.setConsumption(Car.Consumption.valueOf(type.toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    currentCar.setConsumption(null);
                                }
                            }
                        }
                        break;

                    case XMLStreamReader.CHARACTERS:
                        if (currentElement != null) {
                            String text = xmlStreamReader.getText().trim();
                            if (!text.isEmpty()) {
                                parseCarData(currentCar, currentElement, text);
                            }
                        }
                        break;

                    case XMLStreamReader.END_ELEMENT:
                        String endElementName = xmlStreamReader.getLocalName();
                        if ("car".equals(endElementName)) {
                            cars.add(new Car());
                            Car newCar = cars.get(cars.size() - 1);
                            copyCarData(currentCar, newCar);
                        }
                        currentElement = null;
                        break;
                }
            }
            xmlStreamReader.close();
        }
        return cars;
    }

    private void parseCarData(Car car, String element, String text) {
        switch (element) {
            case "manufacturer":
                car.setManufacturer(text);
                break;
            case "model":
                car.setModel(text);
                break;
            case "production-year":
                try {
                    car.setProductionYear(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    car.setProductionYear(0);
                }
                break;
            case "horsepower":
                car.setHorsepower(parseDouble(text));
                break;
            case "consumption":
                car.setConsumptionValue(parseDouble(text));
                break;
            case "price":
                car.setPrice(parseDouble(text));
                break;
        }
    }

    private static double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void copyCarData(Car source, Car target) {
        target.setId(source.getId());
        target.setManufacturer(source.getManufacturer());
        target.setModel(source.getModel());
        target.setProductionYear(source.getProductionYear());
        target.setHorsepower(source.getHorsepower());
        target.setPrice(source.getPrice());
        target.setConsumptionValue(source.getConsumptionValue());
        target.setConsumption(source.getConsumption());
    }
}