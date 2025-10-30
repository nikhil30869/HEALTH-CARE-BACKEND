package com.klef.sdp.controller;

import com.klef.sdp.model.TemperatureData;
import com.klef.sdp.dto.TemperatureReadingDTO;
import com.klef.sdp.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/temperature")
@CrossOrigin(origins = "https://nexuswave-healthcare.netlify.app/")

public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @PostMapping("/add")
    public ResponseEntity<TemperatureData> addTemperatureReading(@RequestBody TemperatureData temperatureData, @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only add data for themselves
            if (temperatureData.getUser() == null || !temperatureData.getUser().getUserId().equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            TemperatureData savedReading = temperatureService.addTemperatureReading(temperatureData);
            return ResponseEntity.ok(savedReading);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TemperatureReadingDTO>> getUserTemperatureReadings(@PathVariable Long userId, @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            List<TemperatureReadingDTO> readings = temperatureService.getUserTemperatureReadingsDTO(userId);
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<TemperatureReadingDTO>> getTemperatureReadingsByRange(
            @PathVariable Long userId,
            @RequestHeader("UserId") Long authenticatedUserId,
            @RequestParam String start,
            @RequestParam String end) {
        try {
            // User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            LocalDateTime startDate = parseDate(start);
            LocalDateTime endDate = parseDate(end);
            
            List<TemperatureReadingDTO> readings = temperatureService.getUserTemperatureReadingsByDateRangeDTO(userId, startDate, endDate);
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getTemperatureStats(@PathVariable Long userId, @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only view their own stats
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            Map<String, Object> stats = temperatureService.getTemperatureStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<TemperatureData> getLatestTemperatureReading(@PathVariable Long userId, @RequestHeader("UserId") Long authenticatedUserId) {
        try {
            // User can only view their own data
            if (!userId.equals(authenticatedUserId)) {
                return ResponseEntity.status(403).build();
            }
            TemperatureData latestReading = temperatureService.getLatestTemperatureReading(userId);
            return latestReading != null ? ResponseEntity.ok(latestReading) : ResponseEntity.notFound().build();
        } catch (Exception e) {
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
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
    }
}