package com.link.hello.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Employee {

    @Id  //primary key
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private Date birthDate;

    @Column(nullable = false)
    private String cnp;

    @Column
    private String email;

    @Column(name="job_id",nullable = false)
    private int idJob;

    @Column(nullable = false)
    private float salary;

    @Column(nullable = false)
    private int age;

}
