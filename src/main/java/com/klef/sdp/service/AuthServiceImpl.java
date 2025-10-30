package com.klef.sdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Override
    public AuthResponse login(String username, String password, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            Object admin = adminService.loginAdmin(username, password);
            if (admin != null) {
                return new AuthResponse("ADMIN", admin);
            }
        } else if ("USER".equalsIgnoreCase(role)) {
            Object user = userService.loginUser(username, password);
            if (user != null) {
                return new AuthResponse("USER", user);
            }
        }
        return null; // invalid login
    }
}
