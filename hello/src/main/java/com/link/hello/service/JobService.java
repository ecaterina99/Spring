package com.link.hello.service;

import com.link.hello.dto.EmployeeDTO;
import com.link.hello.dto.JobDTO;
import com.link.hello.helpers.DTOManager;
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