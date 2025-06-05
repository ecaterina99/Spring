package com.link.hello.service;

import com.link.hello.dto.EmployeeDTO;
import com.link.hello.dto.JobDTO;
import com.link.hello.model.Employee;
import com.link.hello.model.Job;
import com.link.hello.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployeeService employeeService;

    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOs = new ArrayList<>();
        for (Job job : jobs) {
            JobDTO jobDTO = jobToDTO(job);
            jobDTOs.add(jobDTO);
        }
        return jobDTOs;
    }

    JobDTO jobToDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        List<Employee> employees = job.getEmployees();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = employeeService.employeeToDto(employee);
            employeeDTOs.add(employeeDTO);
        }
        jobDTO.setEmployees(employeeDTOs);
        return jobDTO;
    }

}