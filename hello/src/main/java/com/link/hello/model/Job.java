package com.link.hello.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity(name = "jobs")
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String title;

    @Column(name = "minSalary")
    private Float minSalary;

    @Column(name = "maxSalary")
    private Float maxSalary;

    @Column
    private boolean remote;

    @OneToMany(mappedBy="mainJob", cascade=CascadeType.ALL)
    @JsonBackReference
    private List<Employee> employees;
}