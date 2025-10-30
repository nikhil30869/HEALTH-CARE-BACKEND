package com.klef.sdp.repository;

import com.klef.sdp.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsernameAndPassword(String username, String password);
    Admin findByUsername(String username);
    Admin findByEmail(String email);
}