package com.klef.sdp.controller;

import com.klef.sdp.dto.ApiResponseDTO;
import com.klef.sdp.dto.HealthDashboardDTO;
import com.klef.sdp.dto.HealthReadingRequestDTO;
import com.klef.sdp.dto.DateRangeRequestDTO;
import com.klef.sdp.model.*;
import com.klef.sdp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "https://nexuswave-healthcare.netlify.app/")

public class HealthDataController {

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private OxygenService oxygenService;

    @Autowired
    private GlycogenService glycogenService;

    @Autowired
    private BpService bpService;

    @Autowired
    private HeartRateService heartRateService;

    @GetMapping("/user/{userId}/dashboard")
    public ResponseEntity<ApiResponseDTO> getDashboardData(@PathVariable Long userId) {
        try {
            HealthDashboardDTO dashboardData = new HealthDashboardDTO();
            
            // Set latest readings
            dashboardData.setLatestTemperature(temperatureService.getLatestTemperatureReading(userId));
            dashboardData.setLatestOxygen(oxygenService.getLatestOxygenReading(userId));
            dashboardData.setLatestGlycogen(glycogenService.getLatestGlycogenReading(userId));
            dashboardData.setLatestBloodPressure(bpService.getLatestBpReading(userId));
            dashboardData.setLatestHeartRate(heartRateService.getLatestHeartRateReading(userId));
            
            // Set statistics
            dashboardData.setTemperatureStats(temperatureService.getTemperatureStats(userId));
            dashboardData.setOxygenStats(oxygenService.getOxygenStats(userId));
            dashboardData.setGlycogenStats(glycogenService.getGlycogenStats(userId));
            dashboardData.setBpStats(bpService.getBpStats(userId));
            dashboardData.setHeartRateStats(heartRateService.getHeartRateStats(userId));
            
            return ResponseEntity.ok(ApiResponseDTO.success("Dashboard data retrieved successfully", dashboardData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error retrieving dashboard data: " + e.getMessage()));
        }
    }

    @PostMapping("/user/{userId}/add-multiple")
    public ResponseEntity<ApiResponseDTO> addMultipleHealthReadings(
            @PathVariable Long userId,
            @RequestBody HealthReadingRequestDTO healthReadingRequest) {
        
        try {
            Map<String, Object> results = new HashMap<>();
            
            // Add temperature reading if provided
            if (healthReadingRequest.getTemperature() != null) {
                TemperatureData tempData = new TemperatureData();
                User user = new User();
                user.setUserId(userId);
                tempData.setUser(user);
                tempData.setTemperatureValue(healthReadingRequest.getTemperature());
                tempData.setUnit("°F");
                tempData.setMeasuredAt(healthReadingRequest.getMeasuredAt());
                tempData.setNotes(healthReadingRequest.getNotes());
                TemperatureData savedTemp = temperatureService.addTemperatureReading(tempData);
                results.put("temperature", savedTemp);
            }
            
            // Add oxygen reading if provided
            if (healthReadingRequest.getOxygenLevel() != null) {
                OxygenData oxygenData = new OxygenData();
                User user = new User();
                user.setUserId(userId);
                oxygenData.setUser(user);
                oxygenData.setOxygenLevel(healthReadingRequest.getOxygenLevel());
                oxygenData.setUnit("%");
                oxygenData.setMeasuredAt(healthReadingRequest.getMeasuredAt());
                oxygenData.setNotes(healthReadingRequest.getNotes());
                OxygenData savedOxygen = oxygenService.addOxygenReading(oxygenData);
                results.put("oxygen", savedOxygen);
            }
            
            // Add glycogen reading if provided
            if (healthReadingRequest.getGlycogenLevel() != null) {
                GlycogenData glycogenData = new GlycogenData();
                User user = new User();
                user.setUserId(userId);
                glycogenData.setUser(user);
                glycogenData.setGlycogenLevel(healthReadingRequest.getGlycogenLevel());
                glycogenData.setUnit("mg/dL");
                glycogenData.setMeasuredAt(healthReadingRequest.getMeasuredAt());
                glycogenData.setNotes(healthReadingRequest.getNotes());
                GlycogenData savedGlycogen = glycogenService.addGlycogenReading(glycogenData);
                results.put("glycogen", savedGlycogen);
            }
            
            // Add blood pressure reading if provided
            if (healthReadingRequest.getSystolic() != null && healthReadingRequest.getDiastolic() != null) {
                BpData bpData = new BpData();
                User user = new User();
                user.setUserId(userId);
                bpData.setUser(user);
                bpData.setSystolic(healthReadingRequest.getSystolic());
                bpData.setDiastolic(healthReadingRequest.getDiastolic());
                bpData.setPulse(healthReadingRequest.getPulse() != null ? healthReadingRequest.getPulse() : 0);
                bpData.setMeasuredAt(healthReadingRequest.getMeasuredAt());
                bpData.setNotes(healthReadingRequest.getNotes());
                BpData savedBp = bpService.addBpReading(bpData);
                results.put("bloodPressure", savedBp);
            }
            
            // Add heart rate reading if provided
            if (healthReadingRequest.getHeartRate() != null) {
                HeartRateData hrData = new HeartRateData();
                User user = new User();
                user.setUserId(userId);
                hrData.setUser(user);
                hrData.setHeartRate(healthReadingRequest.getHeartRate());
                hrData.setUnit("bpm");
                hrData.setMeasuredAt(healthReadingRequest.getMeasuredAt());
                hrData.setNotes(healthReadingRequest.getNotes());
                HeartRateData savedHr = heartRateService.addHeartRateReading(hrData);
                results.put("heartRate", savedHr);
            }
            
            return ResponseEntity.ok(ApiResponseDTO.success("Health readings added successfully", results));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error adding health readings: " + e.getMessage()));
        }
    }

    @PostMapping("/user/{userId}/date-range")
    public ResponseEntity<ApiResponseDTO> getHealthDataByDateRange(
            @PathVariable Long userId,
            @RequestBody DateRangeRequestDTO dateRangeRequest) {
        
        try {
            Map<String, Object> healthData = new HashMap<>();
            
            healthData.put("temperature", temperatureService.getUserTemperatureReadingsByDateRange(
                userId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()));
            healthData.put("oxygen", oxygenService.getUserOxygenReadingsByDateRange(
                userId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()));
            healthData.put("glycogen", glycogenService.getUserGlycogenReadingsByDateRange(
                userId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()));
            healthData.put("bloodPressure", bpService.getUserBpReadingsByDateRange(
                userId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()));
            healthData.put("heartRate", heartRateService.getUserHeartRateReadingsByDateRange(
                userId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()));
            
            return ResponseEntity.ok(ApiResponseDTO.success("Health data retrieved for date range", healthData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("Error retrieving health data: " + e.getMessage()));
        }
    }
}