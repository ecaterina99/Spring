package com.link.EmployeesWebApp.service;


import com.link.EmployeesWebApp.dto.EmployeeDTO;
import com.link.EmployeesWebApp.helpers.DTOManager;
import com.link.EmployeesWebApp.helpers.EmployeePager;
import com.link.EmployeesWebApp.helpers.EmployeeSorter;
import com.link.EmployeesWebApp.model.Employee;
import com.link.EmployeesWebApp.model.Job;
import com.link.EmployeesWebApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeRepositoryCrud employeeRepositoryCrud;
    @Autowired
    private EmployeeRepositoryJdbc employeeRepositoryJdbc;
    @Autowired
    private EmployeeRepositoryJpa employeeRepositoryJpa;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private DTOManager dtoManager;

    final float richBase = 10 * 1000; // EUR

    private EmployeeDTO employeeToDto(Employee employee) {
        return dtoManager.employeeToDto(employee, true);
    }

    public Employee employeeDtoToModel(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setSalary(employeeDTO.getSalary());
        String[] nameParts = employeeDTO.getFullName().split(" ");
        employee.setFirstName(nameParts[0]);
        employee.setLastName(nameParts[1]);
        employee.setAge(employeeDTO.getAge() != 0 ? employeeDTO.getAge() : -1);
        employee.setMainJob(null); // TODO: check if main job is set
        // TODO: other fields
        List<Integer> idJobs = Collections.singletonList(employeeDTO.getJobId());
        if (idJobs != null && !idJobs.isEmpty()) {
            List<Job> jobs = jobRepository.findAllById(idJobs);
            employee.setAllJobs(jobs);
        }
        return employee;
    }

    private List<EmployeeDTO> employeesToDTOs(List<Employee> employees) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = employeeToDto(employee);
            employeeDTOs.add(employeeDTO);
        }
        return employeeDTOs;
    }

    //with CRUD repository


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
        while (iteratorEmployee.hasNext()) {
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

        if (employee.getSalary() != 0) {
            employeeDatabase.setSalary(employee.getSalary());
        }

        employee = employeeRepositoryCrud.save(employeeDatabase);
        return employeeToDto(employee);
    }

    public long count() {
        return employeeRepositoryCrud.count();
    }

    public boolean delete(int id) {
        employeeRepositoryCrud.deleteById(id);
        Optional<Employee> optionalEmployee = employeeRepositoryCrud.findById(id);
        return optionalEmployee.isEmpty();
    }

    //with JPA repository

    public List<EmployeeDTO> findAllAndMainJob() {
        List<Sort.Order> employeesSort = new ArrayList<>();
        Sort.Order lastNameOrder = new Sort.Order(Sort.Direction.DESC, "lastName");
        Sort.Order firstNameOrder = new Sort.Order(Sort.Direction.ASC, "firstName");
        employeesSort.add(lastNameOrder); //  DESC
        employeesSort.add(firstNameOrder); //  "firstName" ASC
        Sort sort = new EmployeeSorter(employeesSort);
//        List<Employee> employees = employeeRepositoryJpa.findAll(sort);

        EmployeePager employeePager = new EmployeePager(0, 20, sort);
        Page<Employee> employeesPage = employeeRepositoryJpa.findAll(employeePager);
        List<Employee> employees = employeesPage.getContent();

        return employeesToDTOs(employees);
    }


    public EmployeeDTO find(String lastName) {
        Employee employee = employeeRepositoryJpa.findFirstByLastNameContainsOrderByAgeDescFirstNameDesc(lastName);
        return employeeToDto(employee);
    }

    //http://localhost:9000/employee/search?lastName=Bujor
    public List<EmployeeDTO> search(String lastName) {
        List<Employee> employees = employeeRepositoryJpa.findFirstNameDistinctByLastNameContains(lastName);
        return employeesToDTOs(employees);
    }

    //http://localhost:9000/employee/search?firstName=Dorin&lastName=Bujor
    public List<EmployeeDTO> search(String lastName, String firstName) {
        List<Employee> employees = employeeRepositoryJpa.findByLastNameContainsAndFirstNameContains(lastName, firstName);
        return employeesToDTOs(employees);
    }

    //http://localhost:9000/employee/search?startAge=20&endAge=50
    public List<EmployeeDTO> search(int age1, int age2) {
        Set<Employee> employees = employeeRepositoryJpa.findByAgeIsBetween(age1, age2);
        List<Employee> employeeList = new ArrayList<>();
        employeeList.addAll(employees);
        return employeesToDTOs(employeeList);
    }

    public int countByLastName(String lastName) {
        return employeeRepositoryJpa.countByLastNameContains(lastName);
    }

    public int deleteByAge(int age) {
        return employeeRepositoryJpa.deleteByAge(age);
    }

    public List<EmployeeDTO> findOldEmployees(int age) {
        List<Employee> oldEmployees = employeeRepositoryJpa.findOldRichEmployeesNative(age, richBase);
        return employeesToDTOs(oldEmployees);
    }

    public List<Employee> findModelAllAndMainJob() {
        return employeeRepositoryJpa.findAll();
    }


    //JDBC
    public List<EmployeeDTO> findAllJdbc() {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        List<Employee> employees = employeeRepositoryJdbc.findAll();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = employeeToDto(employee);
            employeeDTOList.add(employeeDTO);
        }
        return employeeDTOList;
    }

    public EmployeeDTO findJdbc(int id) {
        Employee employee = employeeRepositoryJdbc.find(id);
        return employeeToDto(employee);
    }

    public EmployeeDTO addEmployeeJdbc(EmployeeDTO employeeDTO) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepositoryJdbc.add(employee);
        return employeeToDto(employee);
    }

    public EmployeeDTO updateJdbc(EmployeeDTO employeeDTO, int id) {
        Employee employee = employeeDtoToModel(employeeDTO);
        employee = employeeRepositoryJdbc.update(employee, id);
        return employeeToDto(employee);
    }

    public boolean deleteJdbc(int id) {
        return employeeRepositoryJdbc.delete(id);
    }

}

