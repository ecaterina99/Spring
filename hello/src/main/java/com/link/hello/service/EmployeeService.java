package com.link.hello.service;

import com.link.hello.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.link.hello.repository.EmployeeRepository;

import java.sql.SQLException;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeRepository.findAll();
    }

    public Employee find(int id) {
        Employee employee = employeeRepository.find(id);
        return employeeRepository.find(id);
    }

   /* private Employee employeeToDto(Employee employee) {
        Employee employeeDTO = new Employee();
        employeeDTO.setId(employee.getId());
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setFullName(
                employee.getFirstName() + " " + employee.getLastName()
        );
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setSalaryInEur(salaryToEur(employee.getSalary()));

        return employeeDTO;
    }

    */

}
