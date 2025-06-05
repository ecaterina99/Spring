package com.link.hello.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "employees_jobs")
@Getter
@Setter
public class EmployeeJob {

    @Id
    @GeneratedValue
    public int id;

    @Column(name = "id_employee")
    public int idEmployee;

    @Column(name = "id_job")
    public int idJob;

}