package com.example.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.job;
import com.example.demo.repository.jobRepository;

import java.util.List;

@Service
public class jobService {

    @Autowired
    private jobRepository jobRepo;

    public job postJob(job job){
        return jobRepo.save(job);
    }

    public List<job> getAllJobs(){
        return jobRepo.findAll();
    }

    public job getJob(Long id){
        return jobRepo.findById(id).orElse(null);
    }
}
