package com.klef.sdp.service;

import com.klef.sdp.model.HeartRateData;
import com.klef.sdp.model.User;
import com.klef.sdp.dto.HeartRateReadingDTO;
import com.klef.sdp.repository.HeartRateRepository;
import com.klef.sdp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeartRateServiceImpl implements HeartRateService {

    @Autowired
    private HeartRateRepository heartRateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public HeartRateData addHeartRateReading(HeartRateData heartRateData) {
        try {
            System.out.println("Service: Adding heart rate reading for user: " + heartRateData.getUser().getUserId());
            
            // Validate user exists
            Optional<User> user = userRepository.findById(heartRateData.getUser().getUserId());
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + heartRateData.getUser().getUserId());
                throw new RuntimeException("User not found");
            }
            
            // Set current time if not provided
            if (heartRateData.getMeasuredAt() == null) {
                heartRateData.setMeasuredAt(LocalDateTime.now());
                System.out.println("Service: Set measuredAt to current time: " + heartRateData.getMeasuredAt());
            }
            
            HeartRateData saved = heartRateRepository.save(heartRateData);
            System.out.println("Service: Successfully saved heart rate reading with ID: " + saved.getHrId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding heart rate reading:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HeartRateData addHeartRateReading(Long userId, Integer heartRate, String unit, LocalDateTime measuredAt, String notes) {
        try {
            System.out.println("Service: Adding heart rate reading for user: " + userId);
            
            // Validate user exists
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + userId);
                throw new RuntimeException("User not found");
            }
            
            // Create HeartRateData entity
            HeartRateData heartRateData = new HeartRateData();
            heartRateData.setUser(user.get());
            heartRateData.setHeartRate(heartRate);
            heartRateData.setUnit(unit);
            heartRateData.setMeasuredAt(measuredAt);
            heartRateData.setNotes(notes);
            
            HeartRateData saved = heartRateRepository.save(heartRateData);
            System.out.println("Service: Successfully saved heart rate reading with ID: " + saved.getHrId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding heart rate reading:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<HeartRateData> getUserHeartRateReadings(Long userId) {
        try {
            System.out.println("Service: Getting all readings for user " + userId);
            List<HeartRateData> readings = heartRateRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            System.out.println("Service: Found " + readings.size() + " total readings for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserHeartRateReadings for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<HeartRateData> getUserHeartRateReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting readings for user " + userId + " between " + start + " and " + end);
            List<HeartRateData> readings = heartRateRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            System.out.println("Service: Found " + readings.size() + " readings in range for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserHeartRateReadingsByDateRange for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    // DTO METHODS
    @Override
    public List<HeartRateReadingDTO> getUserHeartRateReadingsDTO(Long userId) {
        try {
            System.out.println("Service: Getting all readings as DTO for user " + userId);
            List<HeartRateData> readings = heartRateRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            
            // Convert to DTO
            List<HeartRateReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserHeartRateReadingsDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<HeartRateReadingDTO> getUserHeartRateReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting range readings as DTO for user " + userId + " between " + start + " and " + end);
            List<HeartRateData> readings = heartRateRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            
            // Convert to DTO
            List<HeartRateReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings in range for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserHeartRateReadingsByDateRangeDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    private HeartRateReadingDTO convertToDTO(HeartRateData entity) {
        if (entity == null) {
            return null;
        }
        
        HeartRateReadingDTO dto = new HeartRateReadingDTO();
        dto.setHrId(entity.getHrId());
        dto.setHeartRate(entity.getHeartRate());
        dto.setUnit(entity.getUnit());
        dto.setMeasuredAt(entity.getMeasuredAt());
        dto.setNotes(entity.getNotes());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        return dto;
    }

    @Override
    public Map<String, Object> getHeartRateStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            System.out.println("Service: Calculating heart rate stats for user " + userId);
            
            // Initialize with default values
            stats.put("current", 0);
            stats.put("average", 0);
            stats.put("min", 0);
            stats.put("max", 0);
            stats.put("trend", "stable");
            
            // Get all readings for the user
            List<HeartRateData> allReadings = getUserHeartRateReadings(userId);
            
            if (allReadings.isEmpty()) {
                System.out.println("Service: No readings found for user " + userId + ", returning default stats");
                return stats;
            }
            
            // Get latest reading for current heart rate
            HeartRateData latestReading = allReadings.get(0);
            int currentRate = latestReading.getHeartRate();
            stats.put("current", currentRate);
            System.out.println("Service: Current heart rate: " + currentRate);
            
            // Calculate average, min, max from all readings
            double average = allReadings.stream()
                .mapToInt(HeartRateData::getHeartRate)
                .average()
                .orElse(0.0);
            
            int max = allReadings.stream()
                .mapToInt(HeartRateData::getHeartRate)
                .max()
                .orElse(0);
                
            int min = allReadings.stream()
                .mapToInt(HeartRateData::getHeartRate)
                .min()
                .orElse(0);
            
            stats.put("average", Math.round(average));
            stats.put("max", max);
            stats.put("min", min);
            
            System.out.println("Service: Average: " + average + ", Min: " + min + ", Max: " + max);
            
            // Calculate trend (compare with previous reading if available)
            if (allReadings.size() > 1) {
                int current = allReadings.get(0).getHeartRate();
                int previous = allReadings.get(1).getHeartRate();
                System.out.println("Service: Trend calculation - Current: " + current + ", Previous: " + previous);
                
                if (current > previous + 5) {
                    stats.put("trend", "up");
                    System.out.println("Service: Trend: UP");
                } else if (current < previous - 5) {
                    stats.put("trend", "down");
                    System.out.println("Service: Trend: DOWN");
                } else {
                    stats.put("trend", "stable");
                    System.out.println("Service: Trend: STABLE");
                }
            } else {
                System.out.println("Service: Only one reading, trend set to stable");
                stats.put("trend", "stable");
            }
            
            System.out.println("Service: Final heart rate stats for user " + userId + ": " + stats);
            
        } catch (Exception e) {
            System.err.println("Service: Error calculating heart rate stats for user " + userId + ":");
            e.printStackTrace();
            // Return default stats on error
            stats.put("current", 0);
            stats.put("average", 0);
            stats.put("min", 0);
            stats.put("max", 0);
            stats.put("trend", "stable");
        }
        
        return stats;
    }

    @Override
    public HeartRateData getLatestHeartRateReading(Long userId) {
        try {
            System.out.println("Service: Getting latest reading for user " + userId);
            List<HeartRateData> readings = heartRateRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            HeartRateData latest = readings.isEmpty() ? null : readings.get(0);
            System.out.println("Service: Latest reading: " + (latest != null ? latest.getHeartRate() + " bpm" : "null"));
            return latest;
        } catch (Exception e) {
            System.err.println("Service: Error getting latest reading for user " + userId + ":");
            e.printStackTrace();
            return null;
        }
    }
}