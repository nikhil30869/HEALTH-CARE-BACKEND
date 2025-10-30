package com.klef.sdp.controller;

import com.klef.sdp.model.LoginRequest;
import com.klef.sdp.model.User;
import com.klef.sdp.service.AuthService;
import com.klef.sdp.service.UserService;
import com.klef.sdp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            String role = loginRequest.getRole();

            Object result = authService.login(username, password, role);

            if (result == null) {
                return ResponseEntity.badRequest().body("Invalid username, password, or role");
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed due to server error");
        }
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody com.klef.sdp.model.Admin admin) {
        try {
            com.klef.sdp.model.Admin createdAdmin = adminService.createAdmin(admin);
            return ResponseEntity.ok(createdAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin registration failed: " + e.getMessage());
        }
    }
}
