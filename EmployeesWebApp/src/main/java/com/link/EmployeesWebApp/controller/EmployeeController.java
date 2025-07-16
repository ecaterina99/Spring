package com.link.EmployeesWebApp.controller;

import com.link.EmployeesWebApp.dto.EmployeeDTO;
import com.link.EmployeesWebApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    // Return all employees: GET /employee/
    @GetMapping("/")
    public ModelAndView listEmployees() {
        List<EmployeeDTO> employeeDTOs = employeeService.findAllAndMainJob();
        return new ModelAndView("employees", "allEmployees", employeeDTOs);
    }

    @GetMapping("/{id}")
    public ModelAndView showEmployeeById(@PathVariable("id") int id) {
        EmployeeDTO employeeDTO = employeeService.find(id);
        return new ModelAndView("employees/view", "employee", employeeDTO);
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView editEmployeeById(@PathVariable("id") int id,
                                         //   @RequestParam(value="birthDate",required = false) Date birthDate,
                                         @RequestParam(value="fullName", required = false) String fullName,
                                         @RequestParam(required = false) Float salary,
                                         @RequestParam(value = "salaryInEur", required = false) Float salaryInEur,
                                         @RequestParam(required = false) Integer age,
                                         @RequestParam(required = false) String update
    ) {
        if (update==null) {
            EmployeeDTO employeeDTO = employeeService.find(id);
            return new ModelAndView("employees/edit", "employee", employeeDTO);
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFullName(fullName);
        employeeDTO.setId(id);
        employeeDTO.setSalary(salary);
        employeeDTO.setSalaryInEur(salaryInEur);
        employeeDTO.setAge(age);    
        EmployeeDTO employeeDTOdb = employeeService.update(employeeDTO, id);
        return new ModelAndView("employees/view", "employee", employeeDTOdb);

    }

}
