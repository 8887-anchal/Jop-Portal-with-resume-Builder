package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.job;
import com.example.demo.entity.Application;
import com.example.demo.repository.jobRepository;
import com.example.demo.repository.ApplicationRepository;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
//@CrossOrigin(origins = "http://localhost:4200")
public class RecruiterController {

    @Autowired
    private jobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    // Recruiter posts job
    @PostMapping("/post-job")
    public job postJob(@RequestBody job job){
        return jobRepository.save(job);
    }

    // Recruiter view posted jobs
    @GetMapping("/jobs")
    public List<job> getRecruiterJobs(){
        return jobRepository.findAll();
    }

    // Recruiter view applications
    @GetMapping("/applications")
    public List<Application> getApplications(){
        return applicationRepository.findAll();
    }

}
