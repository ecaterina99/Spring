package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "email", nullable = false)
    private String email;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "phone", nullable = false)
    private String phone;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "country")
    private String country = "Romania";
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "city", nullable = false)
    private String city;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "address", nullable = false)
    private String address;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "postal_сode", nullable = false)
    private String postalCode;
    @NotEmpty(message = "This field can't be empty")
    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Sale> sales;
}
