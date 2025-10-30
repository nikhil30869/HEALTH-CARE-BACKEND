package com.klef.sdp.service;

import com.klef.sdp.model.Admin;
import com.klef.sdp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin loginAdmin(String username, String password) {
        // Try to find by username
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    // ... rest of your existing admin methods ...
    @Override
    public Admin createAdmin(Admin admin) {
        // Check if username already exists
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // Check if email already exists
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin updateAdmin(Long adminId, Admin admin) {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin existingAdmin = optionalAdmin.get();
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setEmail(admin.getEmail());
            return adminRepository.save(existingAdmin);
        } else {
            throw new RuntimeException("Admin not found with id: " + adminId);
        }
    }

    @Override
    public void deleteAdmin(Long adminId) {
        if (adminRepository.existsById(adminId)) {
            adminRepository.deleteById(adminId);
        } else {
            throw new RuntimeException("Admin not found with id: " + adminId);
        }
    }
}