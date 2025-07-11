package com.link.CarsFilterWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarsFilterWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsFilterWebAppApplication.class, args);
		System.out.println("Car Filter Web Application started successfully!");
		System.out.println("Access the application at: http://localhost:8080/cars/");
    }

}
