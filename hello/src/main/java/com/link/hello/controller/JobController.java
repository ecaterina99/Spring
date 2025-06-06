package com.link.hello.controller;

import com.link.hello.dto.JobDTO;
import com.link.hello.model.Job;
import com.link.hello.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public List<JobDTO> allJobs() {
        return jobService.findAll();
    }

    @GetMapping("/model")
    public List<Job> allModelJobs() {
        return jobService.findModelAll();
    }


}
