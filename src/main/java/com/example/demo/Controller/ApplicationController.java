package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Application;
import com.example.demo.repository.ApplicationRepository;

@RestController
@RequestMapping("/api/applications")
//@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationRepository repo;

    @PostMapping("/apply")
    public Application applyJob(@RequestBody Application application){
        return repo.save(application);
    }
}