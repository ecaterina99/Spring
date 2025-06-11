package com.example.shop.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
public class Product {

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name = "stock_quantity")
    private int quantity=0;
    @Column(name = "barcode")
    private String barcode;
    @Column(name = "image")
    private String image;
}

  /*  public enum Category {
        BOUQUET,
        PLANT,
        GIFT
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;
   */