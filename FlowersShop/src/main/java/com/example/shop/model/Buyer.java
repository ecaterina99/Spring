package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "buyers")
public class Buyer {

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "country")
    private String country = "Romania";

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
   private String address;

    @Column(name = "postal_сode", nullable = false)
    private String postalCode;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @JoinTable(
            name = "sales",
            joinColumns = @JoinColumn(name = "buyer_id"),
            inverseJoinColumns = @JoinColumn(name = "prouct_id")
    )
    private List<Product> allProducts;
}
