package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "sales")
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue
    public int id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonBackReference
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(nullable = false)
    private int quantity;


}
