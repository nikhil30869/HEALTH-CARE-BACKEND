package com.klef.sdp.service;

import com.klef.sdp.dto.UserUpdateRequestDTO;
import com.klef.sdp.model.User;
import com.klef.sdp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loginUser(String username, String password) {
        // Try to find by username first
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ... rest of your existing methods remain the same ...
    @Override
    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId, UserUpdateRequestDTO updateRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            // Check if username already exists for another user
            User existingUserWithUsername = userRepository.findByUsername(updateRequest.getUsername());
            if (existingUserWithUsername != null && !existingUserWithUsername.getUserId().equals(userId)) {
                throw new RuntimeException("Username already exists");
            }

            // Check if email already exists for another user
            User existingUserWithEmail = userRepository.findByEmail(updateRequest.getEmail());
            if (existingUserWithEmail != null && !existingUserWithEmail.getUserId().equals(userId)) {
                throw new RuntimeException("Email already exists");
            }

            user.setUsername(updateRequest.getUsername());
            user.setEmail(updateRequest.getEmail());
            user.setLocation(updateRequest.getLocation());
            
            if (updateRequest.getDateOfBirth() != null && !updateRequest.getDateOfBirth().trim().isEmpty()) {
                user.setDateOfBirth(updateRequest.getDateOfBirth());
            }
            
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}