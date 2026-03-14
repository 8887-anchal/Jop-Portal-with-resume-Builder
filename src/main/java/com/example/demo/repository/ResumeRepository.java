package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ResumeEntity;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
}
