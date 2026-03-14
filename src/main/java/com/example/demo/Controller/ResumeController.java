package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.ResumeEntity;
import com.example.demo.repository.ResumeRepository;

@RestController
@RequestMapping("/api/resume")
//@CrossOrigin
public class ResumeController {

    @Autowired
    private ResumeRepository repo;

    @PostMapping("/create")
    public ResumeEntity createResume(@RequestBody ResumeEntity resume){
        return repo.save(resume);
    }

    @GetMapping("/{id}")
    public ResumeEntity getResume(@PathVariable Long id){
        return repo.findById(id).orElse(null);
    }
}