package com.link.hello.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllEmployeesDTO {

    List<EmployeeDTO> employees;

    long count;

}
