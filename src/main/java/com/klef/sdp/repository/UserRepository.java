package com.klef.sdp.repository;

import com.klef.sdp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    User findByEmail(String email);
    
    // Check if username exists excluding current user
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.userId != :userId")
    User findByUsernameExcludingCurrentUser(@Param("username") String username, @Param("userId") Long userId);
    
    // Check if email exists excluding current user
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.userId != :userId")
    User findByEmailExcludingCurrentUser(@Param("email") String email, @Param("userId") Long userId);
}