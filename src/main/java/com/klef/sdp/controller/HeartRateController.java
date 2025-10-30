package com.klef.sdp.controller;

import com.klef.sdp.model.HeartRateData;
import com.klef.sdp.dto.HeartRateReadingDTO;
import com.klef.sdp.service.HeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/heart-rate")
@CrossOrigin("*")
public class HeartRateController {

    @Autowired
    private HeartRateService heartRateService;

    @PostMapping("/add")
    public ResponseEntity<HeartRateData> addHeartRateReading(
            @RequestBody Map<String, Object> requestData, 
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            System.out.println("🟡 Heart Rate Controller: Received heart rate data");
            System.out.println("🟡 User ID from header: " + authenticatedUserId);
            System.out.println("🟡 Request Data: " + requestData);
            
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Integer heartRate = Integer.valueOf(requestData.get("heartRate").toString());
            String unit = requestData.get("unit").toString();
            String measuredAtStr = requestData.get("measuredAt").toString();
            String notes = requestData.get("notes") != null ? requestData.get("notes").toString() : "";
            
            // SECURITY: User can only add data for themselves
            if (!userId.equals(authenticatedUserId)) {
                System.out.println("❌ Access denied - user mismatch");
                return ResponseEntity.status(403).build();
            }
            
            LocalDateTime measuredAt = parseDate(measuredAtStr);
            
            System.out.println("🟡 Heart Rate: " + heartRate);
            System.out.println("🟡 Unit: " + unit);
            System.out.println("🟡 Measured At: " + measuredAt);
            System.out.println("🟡 Notes: " + notes);
            
            System.out.println("POST /api/heart-rate/add - Adding heart rate reading");
            HeartRateData savedReading = heartRateService.addHeartRateReading(userId, heartRate, unit, measuredAt, notes);
            return ResponseEntity.ok(savedReading);
        } catch (Exception e) {
            System.err.println("❌ Heart Rate Controller Exception:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HeartRateReadingDTO>> getUserHeartRateReadings(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + " - Fetching all heart rate data as DTO");
            List<HeartRateReadingDTO> readings = heartRateService.getUserHeartRateReadingsDTO(userId);
            System.out.println("Found " + readings.size() + " DTO readings");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<HeartRateReadingDTO>> getHeartRateReadingsByRange(
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
            
            List<HeartRateReadingDTO> readings = heartRateService.getUserHeartRateReadingsByDateRangeDTO(userId, startDate, endDate);
            System.out.println("Found " + readings.size() + " DTO readings in range");
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            System.err.println("ERROR parsing dates:");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getHeartRateStats(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own stats
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            System.out.println("GET /user/" + userId + "/stats");
            Map<String, Object> stats = heartRateService.getHeartRateStats(userId);
            System.out.println("Stats: " + stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<HeartRateData> getLatestHeartRateReading(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // SECURITY: User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            
            HeartRateData latestReading = heartRateService.getLatestHeartRateReading(userId);
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