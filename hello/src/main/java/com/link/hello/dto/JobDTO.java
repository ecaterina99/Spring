package com.link.hello.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobDTO {

    private int id;

    private String title;

    private List<EmployeeDTO> employees;

}
