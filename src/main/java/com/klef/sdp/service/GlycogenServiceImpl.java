package com.klef.sdp.service;

import com.klef.sdp.model.GlycogenData;
import com.klef.sdp.model.User;
import com.klef.sdp.dto.GlycogenReadingDTO;
import com.klef.sdp.repository.GlycogenRepository;
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
public class GlycogenServiceImpl implements GlycogenService {

    @Autowired
    private GlycogenRepository glycogenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public GlycogenData addGlycogenReading(GlycogenData glycogenData) {
        try {
            System.out.println("Service: Adding glycogen reading for user: " + glycogenData.getUser().getUserId());
            
            // Validate user exists
            Optional<User> user = userRepository.findById(glycogenData.getUser().getUserId());
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + glycogenData.getUser().getUserId());
                throw new RuntimeException("User not found");
            }
            
            // Set current time if not provided
            if (glycogenData.getMeasuredAt() == null) {
                glycogenData.setMeasuredAt(LocalDateTime.now());
                System.out.println("Service: Set measuredAt to current time: " + glycogenData.getMeasuredAt());
            }
            
            GlycogenData saved = glycogenRepository.save(glycogenData);
            System.out.println("Service: Successfully saved glycogen reading with ID: " + saved.getGlycogenId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding glycogen reading:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<GlycogenData> getUserGlycogenReadings(Long userId) {
        try {
            System.out.println("Service: Getting all readings for user " + userId);
            List<GlycogenData> readings = glycogenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            System.out.println("Service: Found " + readings.size() + " total readings for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserGlycogenReadings for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<GlycogenData> getUserGlycogenReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting readings for user " + userId + " between " + start + " and " + end);
            List<GlycogenData> readings = glycogenRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            System.out.println("Service: Found " + readings.size() + " readings in range for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserGlycogenReadingsByDateRange for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    // DTO METHODS
    @Override
    public List<GlycogenReadingDTO> getUserGlycogenReadingsDTO(Long userId) {
        try {
            System.out.println("Service: Getting all readings as DTO for user " + userId);
            List<GlycogenData> readings = glycogenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            
            // Convert to DTO
            List<GlycogenReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserGlycogenReadingsDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<GlycogenReadingDTO> getUserGlycogenReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting range readings as DTO for user " + userId + " between " + start + " and " + end);
            List<GlycogenData> readings = glycogenRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            
            // Convert to DTO
            List<GlycogenReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings in range for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserGlycogenReadingsByDateRangeDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    private GlycogenReadingDTO convertToDTO(GlycogenData entity) {
        if (entity == null) {
            return null;
        }
        
        GlycogenReadingDTO dto = new GlycogenReadingDTO();
        dto.setGlycogenId(entity.getGlycogenId());
        dto.setGlycogenLevel(entity.getGlycogenLevel());
        dto.setUnit(entity.getUnit());
        dto.setMeasuredAt(entity.getMeasuredAt());
        dto.setNotes(entity.getNotes());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        return dto;
    }

    @Override
    public Map<String, Object> getGlycogenStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            System.out.println("Service: Calculating glycogen stats for user " + userId);
            
            // Initialize with default values
            stats.put("current", 0.0);
            stats.put("average", 0.0);
            stats.put("min", 0.0);
            stats.put("max", 0.0);
            stats.put("trend", "stable");
            
            // Get all readings for the user
            List<GlycogenData> allReadings = getUserGlycogenReadings(userId);
            
            if (allReadings.isEmpty()) {
                System.out.println("Service: No readings found for user " + userId + ", returning default stats");
                return stats;
            }
            
            // Get latest reading for current glycogen level
            GlycogenData latestReading = allReadings.get(0);
            double currentLevel = latestReading.getGlycogenLevel();
            stats.put("current", Math.round(currentLevel * 10.0) / 10.0);
            System.out.println("Service: Current glycogen level: " + currentLevel);
            
            // Calculate average, min, max from all readings
            double average = allReadings.stream()
                .mapToDouble(GlycogenData::getGlycogenLevel)
                .average()
                .orElse(0.0);
            
            double max = allReadings.stream()
                .mapToDouble(GlycogenData::getGlycogenLevel)
                .max()
                .orElse(0.0);
                
            double min = allReadings.stream()
                .mapToDouble(GlycogenData::getGlycogenLevel)
                .min()
                .orElse(0.0);
            
            stats.put("average", Math.round(average * 10.0) / 10.0);
            stats.put("max", Math.round(max * 10.0) / 10.0);
            stats.put("min", Math.round(min * 10.0) / 10.0);
            
            System.out.println("Service: Average: " + average + ", Min: " + min + ", Max: " + max);
            
            // Calculate trend (compare with previous reading if available)
            if (allReadings.size() > 1) {
                double current = allReadings.get(0).getGlycogenLevel();
                double previous = allReadings.get(1).getGlycogenLevel();
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
            
            System.out.println("Service: Final glycogen stats for user " + userId + ": " + stats);
            
        } catch (Exception e) {
            System.err.println("Service: Error calculating glycogen stats for user " + userId + ":");
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
    public GlycogenData getLatestGlycogenReading(Long userId) {
        try {
            System.out.println("Service: Getting latest reading for user " + userId);
            List<GlycogenData> readings = glycogenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            GlycogenData latest = readings.isEmpty() ? null : readings.get(0);
            System.out.println("Service: Latest reading: " + (latest != null ? latest.getGlycogenLevel() : "null"));
            return latest;
        } catch (Exception e) {
            System.err.println("Service: Error getting latest reading for user " + userId + ":");
            e.printStackTrace();
            return null;
        }
    }
}