package com.link.hello.service;

import com.link.hello.model.Employee;
import com.link.hello.dto.EmployeeDTO;
import com.link.hello.repository.EmployeeRepositoryJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.link.hello.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepositoryJdbc employeeRepositoryJdbc;

    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        List<Employee> employees = employeeRepositoryJdbc.findAll();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = employeeToDto(employee);
            employeeDTOList.add(employeeDTO);
        }
        return employeeDTOList;
    }

    public EmployeeDTO find(int id) {
        Employee employee = employeeRepositoryJdbc.find(id);
        return employeeToDto(employee);
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepositoryJdbc.add(employee);
        return employeeToDto(employee);
    }

    public EmployeeDTO update(EmployeeDTO employeeDTO, int id) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepositoryJdbc.update(employee, id);
        return employeeToDto(employee);
    }

    public boolean delete(int id) {
        return employeeRepositoryJdbc.delete(id);
    }

    public static Employee employeeDtoToModel(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setSalary(employeeDTO.getSalary());
        String[] nameParts = employeeDTO.getFullName().split(" ");
        employee.setFirstName(nameParts[0]);
        employee.setLastName(nameParts[1]);
        employee.setAge(employeeDTO.getAge() != 0 ? employeeDTO.getAge() : -1);
        return employee;
    }

    private EmployeeDTO employeeToDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFullName(
                employee.getFirstName() + " " + employee.getLastName()
        );
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setSalaryInEur(salaryToEur(employee.getSalary()));
        return employeeDTO;
    }

    private float salaryToEur(float salary) {
        return salary / 5;
    }

}
