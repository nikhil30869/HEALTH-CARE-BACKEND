package com.klef.sdp.service;

import com.klef.sdp.model.TemperatureData;
import com.klef.sdp.model.User;
import com.klef.sdp.repository.TemperatureRepository;
import com.klef.sdp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.klef.sdp.dto.TemperatureReadingDTO;
import java.util.stream.Collectors;

@Service
public class TemperatureServiceImpl implements TemperatureService {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TemperatureData addTemperatureReading(TemperatureData temperatureData) {
        try {
            System.out.println("Service: Adding temperature reading for user: " + temperatureData.getUser().getUserId());
            
            // Validate user exists
            Optional<User> user = userRepository.findById(temperatureData.getUser().getUserId());
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + temperatureData.getUser().getUserId());
                throw new RuntimeException("User not found");
            }
            
            // Set current time if not provided
            if (temperatureData.getMeasuredAt() == null) {
                temperatureData.setMeasuredAt(LocalDateTime.now());
                System.out.println("Service: Set measuredAt to current time: " + temperatureData.getMeasuredAt());
            }
            
            TemperatureData saved = temperatureRepository.save(temperatureData);
            System.out.println("Service: Successfully saved temperature reading with ID: " + saved.getTempId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding temperature reading:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<TemperatureData> getUserTemperatureReadings(Long userId) {
        try {
            System.out.println("Service: Getting all readings for user " + userId);
            List<TemperatureData> readings = temperatureRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            System.out.println("Service: Found " + readings.size() + " total readings for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserTemperatureReadings for user " + userId + ":");
            e.printStackTrace();
            return List.of(); // Return empty list instead of throwing
        }
    }

    @Override
    public List<TemperatureData> getUserTemperatureReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting readings for user " + userId + " between " + start + " and " + end);
            List<TemperatureData> readings = temperatureRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            System.out.println("Service: Found " + readings.size() + " readings in range for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserTemperatureReadingsByDateRange for user " + userId + ":");
            e.printStackTrace();
            return List.of(); // Return empty list instead of throwing
        }
    }

    @Override
    public Map<String, Object> getTemperatureStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            System.out.println("Service: Calculating stats for user " + userId);
            
            // Initialize with default values
            stats.put("current", 0.0);
            stats.put("average", 0.0);
            stats.put("min", 0.0);
            stats.put("max", 0.0);
            stats.put("trend", "stable");
            
            // Get all readings for the user
            List<TemperatureData> allReadings = getUserTemperatureReadings(userId);
            
            if (allReadings.isEmpty()) {
                System.out.println("Service: No readings found for user " + userId + ", returning default stats");
                return stats;
            }
            
            // Get latest reading for current temperature
            TemperatureData latestReading = allReadings.get(0);
            double currentTemp = latestReading.getTemperatureValue();
            stats.put("current", Math.round(currentTemp * 10.0) / 10.0);
            System.out.println("Service: Current temperature: " + currentTemp);
            
            // Calculate average, min, max from all readings
            double average = allReadings.stream()
                .mapToDouble(TemperatureData::getTemperatureValue)
                .average()
                .orElse(0.0);
            
            double max = allReadings.stream()
                .mapToDouble(TemperatureData::getTemperatureValue)
                .max()
                .orElse(0.0);
                
            double min = allReadings.stream()
                .mapToDouble(TemperatureData::getTemperatureValue)
                .min()
                .orElse(0.0);
            
            stats.put("average", Math.round(average * 10.0) / 10.0);
            stats.put("max", Math.round(max * 10.0) / 10.0);
            stats.put("min", Math.round(min * 10.0) / 10.0);
            
            System.out.println("Service: Average: " + average + ", Min: " + min + ", Max: " + max);
            
            // Calculate trend (compare with previous reading if available)
            if (allReadings.size() > 1) {
                double current = allReadings.get(0).getTemperatureValue();
                double previous = allReadings.get(1).getTemperatureValue();
                System.out.println("Service: Trend calculation - Current: " + current + ", Previous: " + previous);
                
                if (current > previous + 0.1) {
                    stats.put("trend", "up");
                    System.out.println("Service: Trend: UP");
                } else if (current < previous - 0.1) {
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
            
            System.out.println("Service: Final stats for user " + userId + ": " + stats);
            
        } catch (Exception e) {
            System.err.println("Service: Error calculating stats for user " + userId + ":");
            e.printStackTrace();
            // Return default stats on error
            stats.put("current", 0.0);
            stats.put("average", 0.0);
            stats.put("min", 0.0);
            stats.put("max", 0.0);
            stats.put("trend", "stable");
        }
        
        return stats;
    }

    @Override
    public TemperatureData getLatestTemperatureReading(Long userId) {
        try {
            System.out.println("Service: Getting latest reading for user " + userId);
            List<TemperatureData> readings = temperatureRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            TemperatureData latest = readings.isEmpty() ? null : readings.get(0);
            System.out.println("Service: Latest reading: " + (latest != null ? latest.getTemperatureValue() : "null"));
            return latest;
        } catch (Exception e) {
            System.err.println("Service: Error getting latest reading for user " + userId + ":");
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<TemperatureReadingDTO> getUserTemperatureReadingsDTO(Long userId) {
        try {
            System.out.println("Service: Getting all readings as DTO for user " + userId);
            List<TemperatureData> readings = temperatureRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            
            // Convert to DTO
            List<TemperatureReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserTemperatureReadingsDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<TemperatureReadingDTO> getUserTemperatureReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting range readings as DTO for user " + userId + " between " + start + " and " + end);
            List<TemperatureData> readings = temperatureRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            
            // Convert to DTO
            List<TemperatureReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings in range for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserTemperatureReadingsByDateRangeDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    private TemperatureReadingDTO convertToDTO(TemperatureData entity) {
        if (entity == null) {
            return null;
        }
        
        return new TemperatureReadingDTO(
            entity.getTempId(),
            entity.getUser() != null ? entity.getUser().getUserId() : null,
            entity.getTemperatureValue(),
            entity.getUnit(),
            entity.getMeasuredAt(),
            entity.getNotes()
        );
    }
    
    
}