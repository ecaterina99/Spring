package com.link.hello.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name="employees")
public class Employee {

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "cnp")
    private String cnp;


 //   @Column(name = "job_id", nullable = false)
 //   private int idJob;

    @Column
    private float salary;

    @Column
    private int age;

}
