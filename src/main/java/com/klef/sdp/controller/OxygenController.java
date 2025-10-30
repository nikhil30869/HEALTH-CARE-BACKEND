package com.klef.sdp.controller;

import com.klef.sdp.model.OxygenData;
import com.klef.sdp.dto.OxygenReadingDTO;
import com.klef.sdp.service.OxygenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oxygen")
@CrossOrigin(origins = "https://whimsical-choux-733d4b.netlify.app")

public class OxygenController {

    @Autowired
    private OxygenService oxygenService;

    @PostMapping("/add")
    public ResponseEntity<OxygenData> addOxygenReading(
            @RequestBody OxygenData oxygenData, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            System.out.println("🟡 Oxygen Controller: Received oxygen data");
            System.out.println("🟡 User ID from header: " + authenticatedUserId);
            System.out.println("🟡 Oxygen Data user: " + (oxygenData.getUser() != null ? oxygenData.getUser().getUserId() : "null"));
            System.out.println("🟡 Oxygen Level: " + oxygenData.getOxygenLevel());
            System.out.println("🟡 Unit: " + oxygenData.getUnit());
            System.out.println("🟡 Measured At: " + oxygenData.getMeasuredAt());
            System.out.println("🟡 Notes: " + oxygenData.getNotes());
            
            // SECURITY: User can only add data for themselves
            if (oxygenData.getUser() == null || !oxygenData.getUser().getUserId().equals(authenticatedUserId)) {
                System.out.println("❌ Access denied - user mismatch");
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("POST /api/oxygen/add - Adding oxygen reading");
            OxygenData savedReading = oxygenService.addOxygenReading(oxygenData);
            return ResponseEntity.ok(savedReading);
        } catch (Exception e) {
            System.err.println("❌ Oxygen Controller Exception:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<OxygenReadingDTO>> getOxygenReadingsByRange(
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
            
            List<OxygenReadingDTO> readings = oxygenService.getUserOxygenReadingsByDateRangeDTO(userId, startDate, endDate);
            System.out.println("Found " + readings.size() + " DTO readings in range");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            System.err.println("ERROR parsing dates:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getOxygenStats(
            @PathVariable Long userId, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own stats
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + "/stats");
            Map<String, Object> stats = oxygenService.getOxygenStats(userId);
            System.out.println("Stats: " + stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<OxygenData> getLatestOxygenReading(
            @PathVariable Long userId, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            OxygenData latestReading = oxygenService.getLatestOxygenReading(userId);
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