package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.job;

public interface jobRepository extends JpaRepository<job, Long> {
}
