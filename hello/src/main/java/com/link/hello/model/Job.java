package com.link.hello.model;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //lazy loading
    @JoinTable(
            name = "employees",
            joinColumns = @JoinColumn(name = "id"),           // Job's primary key
            inverseJoinColumns = @JoinColumn(name = "job_id") // Employee's foreign key
    )
    private List<Employee> employees;

}