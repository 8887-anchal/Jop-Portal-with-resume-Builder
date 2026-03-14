package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.user;
import com.example.demo.entity.job;
import com.example.demo.repository.userRepository;
import com.example.demo.repository.jobRepository;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private jobRepository jobRepository;

    // Get all users
    @GetMapping("/users")
    public List<user> getUsers(){
        return userRepository.findAll();
    }

    // Delete user
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return "User deleted";
    }

    // Get all jobs
    @GetMapping("/jobs")
    public List<job> getJobs(){
        return jobRepository.findAll();
    }

    // Delete job
    @DeleteMapping("/job/{id}")
    public String deleteJob(@PathVariable Long id){
        jobRepository.deleteById(id);
        return "Job deleted";
    }

}
