package com.example.demo.Controller;
import java.util.List;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.userService;
import com.example.demo.entity.user;
import com.example.demo.entity.job;
import com.example.demo.entity.Application;
import com.example.demo.entity.ResumeEntity;
import com.example.demo.repository.jobRepository;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.ResumeRepository;

@RestController
@RequestMapping("/users")
public class userController {

    @Autowired
    private userService userService;
    @Autowired
    private jobRepository jobRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ResumeRepository resumeRepository;

    @PostMapping("/register")
    public user register(@RequestBody user user) {
        return userService.register(user);
    }

    @GetMapping
    public List<user> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Jobs
    @GetMapping("/jobs/all")
    public List<job> getAllJobs() {
        return jobRepository.findAll();
    }

    // ✅ Applications by user
    @GetMapping("/applications/user/{userId}")
    public List<Application> getApplicationsByUser(@PathVariable Long userId) {
        try {
            return applicationRepository.findByUser_Id(userId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ✅ Apply for a job
    @PostMapping("/applications/apply")
    public Application applyJob(@RequestBody Application application) {
        return applicationRepository.save(application);
    }

    // ✅ Resume
    @PostMapping("/resume/create")
    public ResumeEntity createResume(@RequestBody ResumeEntity resume) {
        return resumeRepository.save(resume);
    }

    @GetMapping("/resume/{id}")
    public ResumeEntity getResume(@PathVariable Long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    // ✅ Notifications (stub - returns empty list for now)
    @GetMapping("/notifications/{userId}")
    public List<?> getNotifications(@PathVariable Long userId) {
        return Collections.emptyList();
    }
}
