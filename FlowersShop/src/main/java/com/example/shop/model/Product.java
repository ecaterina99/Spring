package com.example.shop.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
public class Product {

    public enum Category {
        bouquet, plant, gift;

        public static Category fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            String normalizedValue = value.trim().toLowerCase();

            try {
                return Category.valueOf(normalizedValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + value +
                        ". Valid values are: bouquet, plant, gift");
            }
        }
    }

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description",  nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "stock_quantity")
    private int quantity;
    @Column(name = "barcode", nullable = false)
    private String barcode;
    @Column(name = "image_url")
    private String image;

}

