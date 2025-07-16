package com.link.EmployeesWebApp.helpers;

import com.link.EmployeesWebApp.dto.EmployeeDTO;
import com.link.EmployeesWebApp.dto.JobDTO;
import com.link.EmployeesWebApp.model.Employee;
import com.link.EmployeesWebApp.model.Job;
import org.springframework.stereotype.Component;

@Component
public class DTOManager {

    public Object EmployeeDTO(Employee employee) {
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
        return jobDTO;
    }
}
