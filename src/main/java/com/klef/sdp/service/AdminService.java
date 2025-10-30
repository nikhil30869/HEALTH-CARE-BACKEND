package com.klef.sdp.service;

import com.klef.sdp.model.Admin;
import java.util.List;

public interface AdminService {
    Admin createAdmin(Admin admin);
    Admin getAdminById(Long adminId);
    List<Admin> getAllAdmins();
    Admin updateAdmin(Long adminId, Admin admin);
    void deleteAdmin(Long adminId);
    Admin loginAdmin(String username, String password); // Changed from email to username
}