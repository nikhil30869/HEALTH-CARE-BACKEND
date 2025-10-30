package com.klef.sdp.dto;

import java.time.LocalDate;

public class UserUpdateRequestDTO {
    private String username;
    private String email;
    private String dateOfBirth; // Keep as String for frontend compatibility
    private String location;

    // Constructors
    public UserUpdateRequestDTO() {}

    public UserUpdateRequestDTO(String username, String email, String dateOfBirth, String location) {
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.location = location;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}