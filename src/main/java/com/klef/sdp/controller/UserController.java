package com.klef.sdp.controller;

import com.klef.sdp.dto.ApiResponseDTO;
import com.klef.sdp.dto.DashboardDTO;
import com.klef.sdp.dto.UserUpdateRequestDTO;
import com.klef.sdp.model.User;
import com.klef.sdp.service.DashboardService;
import com.klef.sdp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DashboardService dashboardService; // Inject the interface, not the implementation

    // Add this new endpoint for dashboard data
    @GetMapping("/{userId}/dashboard")
    public ResponseEntity<ApiResponseDTO> getDashboardData(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).body(ApiResponseDTO.error("Access denied"));
            }
            DashboardDTO dashboardData = dashboardService.getDashboardData(userId); // Call via service interface
            return ResponseEntity.ok(ApiResponseDTO.success("Dashboard data retrieved successfully", dashboardData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error retrieving dashboard data: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDTO updateRequest,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only update their own profile
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).body(ApiResponseDTO.error("Access denied"));
            }
            User updatedUser = userService.updateUser(userId, updateRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("User updated successfully", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error updating user"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO> getUserById(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only view their own profile
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).body(ApiResponseDTO.error("Access denied"));
            }
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error retrieving user"));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO> deleteUser(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only delete their own profile
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).body(ApiResponseDTO.error("Access denied"));
            }
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponseDTO.success("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error deleting user"));
        }
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponseDTO> getMyProfile(@RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only view their own profile
            User user = userService.getUserById(authenticatedUserId);
            return ResponseEntity.ok(ApiResponseDTO.success("User profile retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error retrieving user profile"));
        }
    }
}