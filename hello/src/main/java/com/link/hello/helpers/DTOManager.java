package com.link.hello.helpers;

import com.link.hello.dto.EmployeeDTO;
import com.link.hello.dto.JobDTO;
import com.link.hello.model.Employee;
import com.link.hello.model.Job;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOManager {

    public EmployeeDTO employeeToDto(Employee employee) {
        return employeeToDto(employee, true);
    }

    public EmployeeDTO employeeToDto(Employee employee, boolean deep) {
        if (employee == null) return null;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFullName(
                employee.getFirstName() + " " + employee.getLastName()
        );
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setSalaryInEur(salaryToEur(employee.getSalary()));
        if (deep) {
            if (employee.getMainJob() != null) {
                Job job = employee.getMainJob();
                employeeDTO.setJob(jobToDTO(job));
            }
        }
        return employeeDTO;
    }

    private float salaryToEur(float salary) {
        return salary / 5;
    }

    public JobDTO jobToDTO(Job job) {
        return jobToDTO(job, true);
    }

    public JobDTO jobToDTO(Job job, boolean deep) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        if (deep) {
//            List<Employee> employees = job.getEmployees();
//            for (Employee employee : employees) {
//                EmployeeDTO employeeDTO = employeeToDto(employee);
//                employeeDTOs.add(employeeDTO);
//            }
//            jobDTO.setEmployees(employeeDTOs);
        }
        return jobDTO;
    }
}
