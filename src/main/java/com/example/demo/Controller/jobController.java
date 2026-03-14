package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.job;
import com.example.demo.Service.jobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")

//@CrossOrigin(origins = "http://localhost:4200")

public class jobController {

    @Autowired
    private jobService jobService;

    @PostMapping("/post")
    public job postJob(@RequestBody job job){
        return jobService.postJob(job);
    }

    @GetMapping("/all")
    public List<job> getJobs(){
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public job getJob(@PathVariable Long id){
        return jobService.getJob(id);
    }
}