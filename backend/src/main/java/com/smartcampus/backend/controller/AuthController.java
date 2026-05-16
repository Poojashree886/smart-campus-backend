package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.User;
import com.smartcampus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ========== Login API ==========
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        String role = loginRequest.get("role");
        
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOptional = userRepository.findByEmailAndRole(email, role);
        
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found with this email and role");
            return ResponseEntity.status(401).body(response);
        }
        
        User user = userOptional.get();
        
        if (!user.getPassword().equals(password)) {
            response.put("success", false);
            response.put("message", "Invalid password");
            return ResponseEntity.status(401).body(response);
        }
        
        String redirectPage = getRedirectPage(role);
        
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", "jwt-token-" + System.currentTimeMillis());
        response.put("redirect", redirectPage);
        response.put("userId", user.getId());
        response.put("role", user.getRole());
        
        return ResponseEntity.ok(response);
    }
    
    private String getRedirectPage(String role) {
        switch(role) {
            case "student": return "/pages/student-dashboard.html";
            case "faculty": return "/pages/faculty-dashboard.html";
            case "admin": return "/pages/admin-dashboard.html";
            case "parent": return "/pages/parent-dashboard.html";
            case "maintenance": return "/pages/maintenance-dashboard.html";
            default: return "/login.html";
        }
    }

    // ========== Register API ==========
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> registerData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = (String) registerData.get("email");
            String password = (String) registerData.get("password");
            String role = (String) registerData.get("role");
            
            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                response.put("success", false);
                response.put("message", "Email already registered");
                return ResponseEntity.status(400).body(response);
            }
            
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("userId", user.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ========== Change Password API ==========
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, Object> passwordData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = Long.parseLong(passwordData.get("userId").toString());
            String oldPassword = (String) passwordData.get("oldPassword");
            String newPassword = (String) passwordData.get("newPassword");
            
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(404).body(response);
            }
            
            User user = userOpt.get();
            
            if (!user.getPassword().equals(oldPassword)) {
                response.put("success", false);
                response.put("message", "Current password is incorrect");
                return ResponseEntity.status(401).body(response);
            }
            
            user.setPassword(newPassword);
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Password changed successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}