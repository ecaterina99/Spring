package com.example.shop.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
public class Product {

    public enum Category {
        BOUQUET,
        PLANT,
        GIFT
    }

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "stock_quantity", nullable = false)
    private int quantity=0;
    @Column(name = "barcode", nullable = false)
    private String barcode;
    @Column(name = "image")
    private String image;
}
