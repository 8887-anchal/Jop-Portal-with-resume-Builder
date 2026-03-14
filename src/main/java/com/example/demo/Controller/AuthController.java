package com.example.demo.Controller;

import com.example.demo.Service.userService;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.user;
import com.example.demo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// ✅ API versioning: /api/v1/auth/...
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Register and login endpoints")
public class AuthController {

    @Autowired
    private userService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Also keep /users/login for backward compatibility with Angular

    @PostMapping({"/login"})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            user foundUser = userService.login(request.getEmail(), request.getPassword());

            if (foundUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }

            String token = jwtUtil.generateToken(request.getEmail());  // likely crashing here

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", foundUser.getId());
            response.put("email", foundUser.getEmail());
            response.put("role", foundUser.getRole() != null ? foundUser.getRole().toUpperCase() : "USER");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();  // ← THIS will print the real error to Spring Boot terminal
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/hash/{raw}")
    public String hashPassword(@PathVariable String raw) {
        return new BCryptPasswordEncoder(10).encode(raw);
    }
    @PostMapping({"/register"})
    @Operation(summary = "Register user", description = "Creates a new user account")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        user newUser = new user();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setPhone(request.getPhone());
        newUser.setRole(request.getRole());

        user saved = userService.register(newUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("id", saved.getId());
        response.put("email", saved.getEmail());
        response.put("role", saved.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}