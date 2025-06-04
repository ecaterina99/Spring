package com.link.hello.service;

import com.link.hello.model.Employee;
import com.link.hello.dto.EmployeeDTO;
import com.link.hello.repository.EmployeeRepositoryCrud;
import com.link.hello.repository.EmployeeRepositoryJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.link.hello.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeRepositoryCrud employeeRepositoryCrud;
    @Autowired
    private EmployeeRepositoryJdbc employeeRepositoryJdbc;

   public EmployeeDTO find(int id) {
       Optional<Employee> employeeOptional = employeeRepositoryCrud.findById(id);
       Employee employee;
       if (employeeOptional.isPresent()) {
           employee = employeeOptional.get();
       } else {
           employee = null;
       }
       return employeeToDto(employee);
   }

    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        Iterable<Employee> iterableEmployees = employeeRepositoryCrud.findAll();
        Iterator<Employee> iteratorEmployee = iterableEmployees.iterator();
        while(iteratorEmployee.hasNext()) {
            Employee employee = iteratorEmployee.next();
            EmployeeDTO employeeDTO = employeeToDto(employee);
            employeeDTOList.add(employeeDTO);
        }
        return employeeDTOList;
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepositoryCrud.save(employee);
        return employeeToDto(employee);
    }

    public EmployeeDTO update(EmployeeDTO employeeDTO, int id) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee.setId(id);
        Employee employeeDatabase = employeeRepositoryCrud.findById(id).orElse(null);
        if (employeeDatabase == null) return null;

        if (employee.getFirstName() != null) {
            employeeDatabase.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            employeeDatabase.setLastName(employee.getLastName());
        }
        // ...
        employee = employeeRepositoryCrud.save(employeeDatabase);
        return employeeToDto(employee);
    }


    public long count(){
        return employeeRepositoryCrud.count();
    }

    public boolean delete(int id) {
        employeeRepositoryCrud.deleteById(id);
        Optional<Employee> optionalEmployee = employeeRepositoryCrud.findById(id);
        return optionalEmployee.isEmpty();
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


   /* JDBC repository
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
    */