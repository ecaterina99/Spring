package com.link.hello.controller;

import com.link.hello.service.EmployeeService;
import com.link.hello.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public List<Employee> getEmployees() throws SQLException {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployees(
            @PathVariable int id
    ) {
        return employeeService.find(id);
    }


}
