package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String location;

    // Constructors
    public User() {}

    public User(Long userId) {
        this.userId = userId;
    }

    public User(String username, String email, String password, LocalDate dateOfBirth, String location) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.location = location;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    // Additional setter for String date (converts String to LocalDate)
    public void setDateOfBirth(String dateString) {
        if (dateString != null && !dateString.trim().isEmpty()) {
            this.dateOfBirth = LocalDate.parse(dateString);
        }
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}