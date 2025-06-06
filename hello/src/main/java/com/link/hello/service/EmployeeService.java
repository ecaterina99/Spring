package com.link.hello.service;

import com.link.hello.dto.JobDTO;
import com.link.hello.helpers.DTOManager;
import com.link.hello.model.Employee;
import com.link.hello.dto.EmployeeDTO;
import com.link.hello.model.Job;
import com.link.hello.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static JobRepository jobRepository;
    @Autowired
    private DTOManager dtoManager;

    final float richBase = 10 * 1000; // EUR

    private EmployeeDTO employeeToDto(Employee employee) {
        return dtoManager.employeeToDto(employee, false);
    }

    public static Employee employeeDtoToModel(EmployeeDTO employeeDTO) {
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
        // ...
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
        //  List<Employee> oldEmployees = employeeRepositoryJpa.findByAgeIsGreaterThanEqual(age);
        //  List<Employee> oldEmployees = employeeRepositoryJpa.findOldEmployees(age);
        return employeesToDTOs(oldEmployees);
    }

    public List<EmployeeDTO> findAllAndMainJob() {
        List<Employee> employees = employeeRepositoryJpa.findAllEmployeesAndMainJob();
        return employeesToDTOs(employees);
    }

    public List<Employee> findModelAllAndMainJob() {
        return employeeRepositoryJpa.findAll();
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