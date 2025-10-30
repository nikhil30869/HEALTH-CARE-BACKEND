package com.klef.sdp.controller;

import com.klef.sdp.model.GlycogenData;
import com.klef.sdp.dto.GlycogenReadingDTO;
import com.klef.sdp.service.GlycogenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/glycogen")
@CrossOrigin(origins = "https://whimsical-choux-733d4b.netlify.app")

public class GlycogenController {

    @Autowired
    private GlycogenService glycogenService;

    @PostMapping("/add")
    public ResponseEntity<GlycogenData> addGlycogenReading(
            @RequestBody GlycogenData glycogenData, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            System.out.println("🟡 Glycogen Controller: Received glycogen data");
            System.out.println("🟡 User ID from header: " + authenticatedUserId);
            System.out.println("🟡 Glycogen Data user: " + (glycogenData.getUser() != null ? glycogenData.getUser().getUserId() : "null"));
            System.out.println("🟡 Glycogen Level: " + glycogenData.getGlycogenLevel());
            System.out.println("🟡 Unit: " + glycogenData.getUnit());
            System.out.println("🟡 Measured At: " + glycogenData.getMeasuredAt());
            System.out.println("🟡 Notes: " + glycogenData.getNotes());
            
            // SECURITY: User can only add data for themselves
            if (glycogenData.getUser() == null || !glycogenData.getUser().getUserId().equals(authenticatedUserId)) {
                System.out.println("❌ Access denied - user mismatch");
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("POST /api/glycogen/add - Adding glycogen reading");
            GlycogenData savedReading = glycogenService.addGlycogenReading(glycogenData);
            return ResponseEntity.ok(savedReading);
        } catch (Exception e) {
            System.err.println("❌ Glycogen Controller Exception:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GlycogenReadingDTO>> getUserGlycogenReadings(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + " - Fetching all glycogen data as DTO");
            List<GlycogenReadingDTO> readings = glycogenService.getUserGlycogenReadingsDTO(userId);
            System.out.println("Found " + readings.size() + " DTO readings");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<GlycogenReadingDTO>> getGlycogenReadingsByRange(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId,
            @RequestParam String start,
            @RequestParam String end) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + "/range");
            LocalDateTime startDate = parseDate(start);
            LocalDateTime endDate = parseDate(end);
            
            List<GlycogenReadingDTO> readings = glycogenService.getUserGlycogenReadingsByDateRangeDTO(userId, startDate, endDate);
            System.out.println("Found " + readings.size() + " DTO readings in range");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            System.err.println("ERROR parsing dates:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getGlycogenStats(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own stats
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + "/stats");
            Map<String, Object> stats = glycogenService.getGlycogenStats(userId);
            System.out.println("Stats: " + stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<GlycogenData> getLatestGlycogenReading(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            GlycogenData latestReading = glycogenService.getLatestGlycogenReading(userId);
            return latestReading != null ? ResponseEntity.ok(latestReading) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private LocalDateTime parseDate(String dateString) {
        try {
            if (dateString.contains(".")) {
                dateString = dateString.substring(0, dateString.indexOf('.')) + "Z";
            }
            OffsetDateTime odt = OffsetDateTime.parse(dateString);
            return odt.toLocalDateTime();
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse date: " + dateString);
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
    }
}