package com.klef.sdp.controller;

import com.klef.sdp.model.BpData;
import com.klef.sdp.dto.BpReadingDTO;
import com.klef.sdp.service.BpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blood-pressure")
@CrossOrigin(origins = "https://nexuswave-healthcare.netlify.app/")

public class BpController {

    @Autowired
    private BpService bpService;

    @PostMapping("/add")
    public ResponseEntity<BpData> addBpReading(
            @RequestBody BpData bpData, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            System.out.println("🟡 BP Controller: Received BP data");
            System.out.println("🟡 User ID from header: " + authenticatedUserId);
            System.out.println("🟡 BP Data user: " + (bpData.getUser() != null ? bpData.getUser().getUserId() : "null"));
            System.out.println("🟡 Systolic: " + bpData.getSystolic());
            System.out.println("🟡 Diastolic: " + bpData.getDiastolic());
            System.out.println("🟡 Pulse: " + bpData.getPulse());
            System.out.println("🟡 Measured At: " + bpData.getMeasuredAt());
            System.out.println("🟡 Notes: " + bpData.getNotes());
            
            // SECURITY: User can only add data for themselves
            if (bpData.getUser() == null || !bpData.getUser().getUserId().equals(authenticatedUserId)) {
                System.out.println("❌ Access denied - user mismatch");
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("POST /api/blood-pressure/add - Adding BP reading");
            BpData savedReading = bpService.addBpReading(bpData);
            return ResponseEntity.ok(savedReading);
        } catch (Exception e) {
            System.err.println("❌ BP Controller Exception:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BpReadingDTO>> getUserBpReadings(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + " - Fetching all BP data as DTO");
            List<BpReadingDTO> readings = bpService.getUserBpReadingsDTO(userId);
            System.out.println("Found " + readings.size() + " DTO readings");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<BpReadingDTO>> getBpReadingsByRange(
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
            System.out.println("Raw Start: " + start);
            System.out.println("Raw End: " + end);
            
            LocalDateTime startDate = parseDate(start);
            LocalDateTime endDate = parseDate(end);
            
            System.out.println("Parsed Start: " + startDate);
            System.out.println("Parsed End: " + endDate);
            
            List<BpReadingDTO> readings = bpService.getUserBpReadingsByDateRangeDTO(userId, startDate, endDate);
            System.out.println("Found " + readings.size() + " DTO readings in range");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            System.err.println("ERROR parsing dates:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getBpStats(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own stats
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + "/stats");
            Map<String, Object> stats = bpService.getBpStats(userId);
            System.out.println("Stats: " + stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<BpData> getLatestBpReading(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            BpData latestReading = bpService.getLatestBpReading(userId);
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