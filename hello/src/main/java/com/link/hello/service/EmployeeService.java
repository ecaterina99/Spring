package com.link.hello.service;

import com.link.hello.model.Employee;
import com.link.hello.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.link.hello.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = employeeToDto(employee);
            employeeDTOList.add(employeeDTO);
        }
        return employeeDTOList;
    }

    public EmployeeDTO find(int id) {
        Employee employee = employeeRepository.find(id);
        return employeeToDto(employee);
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepository.add(employee);
        return employeeToDto(employee);
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

    public boolean delete(int id) {
        return employeeRepository.delete(id);
    }


    public EmployeeDTO update(EmployeeDTO employeeDTO, int id) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepository.update(employee, id);
        return employeeToDto(employee);
    }
}
