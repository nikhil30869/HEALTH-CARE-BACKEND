package com.klef.sdp.service;

import com.klef.sdp.dto.UserUpdateRequestDTO;
import com.klef.sdp.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
    List<User> getAllUsers();
    User updateUser(Long userId, UserUpdateRequestDTO updateRequest);
    void deleteUser(Long userId);
    User loginUser(String username, String password); // Changed from email to username
}