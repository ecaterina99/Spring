package com.link.EmployeesWebApp.service;


import com.link.EmployeesWebApp.dto.JobDTO;
import com.link.EmployeesWebApp.helpers.DTOManager;
import com.link.EmployeesWebApp.model.Job;
import com.link.EmployeesWebApp.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private DTOManager dtoManager;
    @Autowired
    private JobRepository jobRepository;

    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOs = new ArrayList<>();
        for (Job job : jobs) {
            JobDTO jobDTO = dtoManager.jobToDTO(job, false);
            jobDTOs.add(jobDTO);
        }
        return jobDTOs;
    }

    public List<Job> findModelAll() {
        return jobRepository.findAll();
    }
}