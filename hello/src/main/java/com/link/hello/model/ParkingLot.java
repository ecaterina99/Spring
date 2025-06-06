package com.link.hello.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity (name = "parking_lots")
@Getter
@Setter
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "park_num")
    private String parkNumber;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    @JsonBackReference
    private Employee employee;

}

