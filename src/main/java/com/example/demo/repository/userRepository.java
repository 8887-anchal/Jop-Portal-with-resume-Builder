package com.example.demo.repository;

import com.example.demo.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<user, Long> {

    // ✅ Returns Optional<user> — Spring Data standard
    Optional<user> findByEmail(String email);
}