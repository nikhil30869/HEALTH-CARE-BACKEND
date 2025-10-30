package com.klef.sdp.service;

import com.klef.sdp.model.OxygenData;
import com.klef.sdp.model.User;
import com.klef.sdp.dto.OxygenReadingDTO;
import com.klef.sdp.repository.OxygenRepository;
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
public class OxygenServiceImpl implements OxygenService {

    @Autowired
    private OxygenRepository oxygenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OxygenData addOxygenReading(OxygenData oxygenData) {
        try {
            System.out.println("Service: Adding oxygen reading for user: " + oxygenData.getUser().getUserId());
            
            // Validate user exists
            Optional<User> user = userRepository.findById(oxygenData.getUser().getUserId());
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + oxygenData.getUser().getUserId());
                throw new RuntimeException("User not found");
            }
            
            // Set current time if not provided
            if (oxygenData.getMeasuredAt() == null) {
                oxygenData.setMeasuredAt(LocalDateTime.now());
                System.out.println("Service: Set measuredAt to current time: " + oxygenData.getMeasuredAt());
            }
            
            OxygenData saved = oxygenRepository.save(oxygenData);
            System.out.println("Service: Successfully saved oxygen reading with ID: " + saved.getOxygenId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding oxygen reading:");
            e.printStackTrace();
            throw e;
        }
    }

    // Add this method for additional security validation
    public boolean validateUserAccess(Long requestedUserId, Long authenticatedUserId) {
        return requestedUserId.equals(authenticatedUserId);
    }

    @Override
    public List<OxygenData> getUserOxygenReadings(Long userId) {
        try {
            System.out.println("Service: Getting all readings for user " + userId);
            List<OxygenData> readings = oxygenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            System.out.println("Service: Found " + readings.size() + " total readings for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserOxygenReadings for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<OxygenData> getUserOxygenReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting readings for user " + userId + " between " + start + " and " + end);
            List<OxygenData> readings = oxygenRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            System.out.println("Service: Found " + readings.size() + " readings in range for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserOxygenReadingsByDateRange for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<OxygenReadingDTO> getUserOxygenReadingsDTO(Long userId) {
        try {
            System.out.println("Service: Getting all readings as DTO for user " + userId);
            List<OxygenData> readings = oxygenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            
            List<OxygenReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserOxygenReadingsDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<OxygenReadingDTO> getUserOxygenReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting range readings as DTO for user " + userId + " between " + start + " and " + end);
            List<OxygenData> readings = oxygenRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            
            List<OxygenReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings in range for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserOxygenReadingsByDateRangeDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    private OxygenReadingDTO convertToDTO(OxygenData entity) {
        if (entity == null) {
            return null;
        }
        
        OxygenReadingDTO dto = new OxygenReadingDTO();
        dto.setOxygenId(entity.getOxygenId());
        dto.setOxygenLevel(entity.getOxygenLevel());
        dto.setUnit(entity.getUnit());
        dto.setMeasuredAt(entity.getMeasuredAt());
        dto.setNotes(entity.getNotes());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        return dto;
    }

    @Override
    public Map<String, Object> getOxygenStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            System.out.println("Service: Calculating stats for user " + userId);
            
            stats.put("current", 0.0);
            stats.put("average", 0.0);
            stats.put("min", 0.0);
            stats.put("max", 0.0);
            stats.put("trend", "stable");
            
            List<OxygenData> allReadings = getUserOxygenReadings(userId);
            
            if (allReadings.isEmpty()) {
                System.out.println("Service: No readings found for user " + userId + ", returning default stats");
                return stats;
            }
            
            OxygenData latestReading = allReadings.get(0);
            double currentLevel = latestReading.getOxygenLevel();
            stats.put("current", Math.round(currentLevel * 10.0) / 10.0);
            
            double average = allReadings.stream()
                .mapToDouble(OxygenData::getOxygenLevel)
                .average()
                .orElse(0.0);
            
            double max = allReadings.stream()
                .mapToDouble(OxygenData::getOxygenLevel)
                .max()
                .orElse(0.0);
                
            double min = allReadings.stream()
                .mapToDouble(OxygenData::getOxygenLevel)
                .min()
                .orElse(0.0);
            
            stats.put("average", Math.round(average * 10.0) / 10.0);
            stats.put("max", Math.round(max * 10.0) / 10.0);
            stats.put("min", Math.round(min * 10.0) / 10.0);
            
            if (allReadings.size() > 1) {
                double current = allReadings.get(0).getOxygenLevel();
                double previous = allReadings.get(1).getOxygenLevel();
                
                if (current > previous + 0.1) {
                    stats.put("trend", "up");
                } else if (current < previous - 0.1) {
                    stats.put("trend", "down");
                } else {
                    stats.put("trend", "stable");
                }
            } else {
                stats.put("trend", "stable");
            }
            
        } catch (Exception e) {
            System.err.println("Service: Error calculating stats for user " + userId + ":");
            e.printStackTrace();
            stats.put("current", 0.0);
            stats.put("average", 0.0);
            stats.put("min", 0.0);
            stats.put("max", 0.0);
            stats.put("trend", "stable");
        }
        
        return stats;
    }

    @Override
    public OxygenData getLatestOxygenReading(Long userId) {
        try {
            System.out.println("Service: Getting latest reading for user " + userId);
            List<OxygenData> readings = oxygenRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            OxygenData latest = readings.isEmpty() ? null : readings.get(0);
            return latest;
        } catch (Exception e) {
            System.err.println("Service: Error getting latest reading for user " + userId + ":");
            e.printStackTrace();
            return null;
        }
    }
}