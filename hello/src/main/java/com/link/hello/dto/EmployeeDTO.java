package com.link.hello.dto;

//DTO- data transfer object

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class EmployeeDTO {
    private int id;
    private String fullName;
    private Date birthDate;
    private Integer age;
    private float salary;
    private float salaryInEur;
    private int jobId;
    private JobDTO job;
    private List<Integer> idJobs;

}
