package com.example.demo.Service;

import com.example.demo.entity.user;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class userService {

    @Autowired
    private userRepository userRepository;

    // ✅ BCrypt encoder — strength 10 is standard
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    // ✅ Hash password before saving
    public user register(user user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<user> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Compare raw password against hashed password in DB
    public user login(String email, String password) {
        Optional<user> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            user foundUser = optionalUser.get();
            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                return foundUser;
            }
        }
        return null;
    }
}