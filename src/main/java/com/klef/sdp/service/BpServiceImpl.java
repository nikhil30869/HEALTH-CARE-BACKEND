package com.klef.sdp.service;

import com.klef.sdp.model.BpData;
import com.klef.sdp.model.User;
import com.klef.sdp.dto.BpReadingDTO;
import com.klef.sdp.repository.BpRepository;
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
public class BpServiceImpl implements BpService {

    @Autowired
    private BpRepository bpRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BpData addBpReading(BpData bpData) {
        try {
            System.out.println("Service: Adding BP reading for user: " + bpData.getUser().getUserId());
            
            // Validate user exists
            Optional<User> user = userRepository.findById(bpData.getUser().getUserId());
            if (user.isEmpty()) {
                System.err.println("Service: User not found: " + bpData.getUser().getUserId());
                throw new RuntimeException("User not found");
            }
            
            // Set current time if not provided
            if (bpData.getMeasuredAt() == null) {
                bpData.setMeasuredAt(LocalDateTime.now());
                System.out.println("Service: Set measuredAt to current time: " + bpData.getMeasuredAt());
            }
            
            BpData saved = bpRepository.save(bpData);
            System.out.println("Service: Successfully saved BP reading with ID: " + saved.getBpId());
            return saved;
        } catch (Exception e) {
            System.err.println("Service: Error adding BP reading:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<BpData> getUserBpReadings(Long userId) {
        try {
            System.out.println("Service: Getting all readings for user " + userId);
            List<BpData> readings = bpRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            System.out.println("Service: Found " + readings.size() + " total readings for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserBpReadings for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<BpData> getUserBpReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting readings for user " + userId + " between " + start + " and " + end);
            List<BpData> readings = bpRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            System.out.println("Service: Found " + readings.size() + " readings in range for user " + userId);
            return readings;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserBpReadingsByDateRange for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    // DTO METHODS
    @Override
    public List<BpReadingDTO> getUserBpReadingsDTO(Long userId) {
        try {
            System.out.println("Service: Getting all readings as DTO for user " + userId);
            List<BpData> readings = bpRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            
            // Convert to DTO
            List<BpReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserBpReadingsDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<BpReadingDTO> getUserBpReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end) {
        try {
            System.out.println("Service: Getting range readings as DTO for user " + userId + " between " + start + " and " + end);
            List<BpData> readings = bpRepository.findByUserUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end);
            
            // Convert to DTO
            List<BpReadingDTO> dtos = readings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            System.out.println("Service: Found " + dtos.size() + " DTO readings in range for user " + userId);
            return dtos;
        } catch (Exception e) {
            System.err.println("Service: Error in getUserBpReadingsByDateRangeDTO for user " + userId + ":");
            e.printStackTrace();
            return List.of();
        }
    }

    private BpReadingDTO convertToDTO(BpData entity) {
        if (entity == null) {
            return null;
        }
        
        BpReadingDTO dto = new BpReadingDTO();
        dto.setBpId(entity.getBpId());
        dto.setSystolic(entity.getSystolic());
        dto.setDiastolic(entity.getDiastolic());
        dto.setPulse(entity.getPulse());
        dto.setMeasuredAt(entity.getMeasuredAt());
        dto.setNotes(entity.getNotes());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        return dto;
    }

    @Override
    public Map<String, Object> getBpStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            System.out.println("Service: Calculating BP stats for user " + userId);
            
            // Initialize with default values
            stats.put("currentSystolic", 0);
            stats.put("currentDiastolic", 0);
            stats.put("currentPulse", 0);
            stats.put("averageSystolic", 0);
            stats.put("averageDiastolic", 0);
            stats.put("trend", "stable");
            
            // Get all readings for the user
            List<BpData> allReadings = getUserBpReadings(userId);
            
            if (allReadings.isEmpty()) {
                System.out.println("Service: No readings found for user " + userId + ", returning default stats");
                return stats;
            }
            
            // Get latest reading for current BP
            BpData latestReading = allReadings.get(0);
            stats.put("currentSystolic", latestReading.getSystolic());
            stats.put("currentDiastolic", latestReading.getDiastolic());
            stats.put("currentPulse", latestReading.getPulse());
            
            System.out.println("Service: Current BP: " + latestReading.getSystolic() + "/" + latestReading.getDiastolic());
            
            // Calculate averages from repository queries
            Double avgSystolic = bpRepository.findAverageSystolicByUserId(userId);
            Double avgDiastolic = bpRepository.findAverageDiastolicByUserId(userId);
            
            stats.put("averageSystolic", avgSystolic != null ? Math.round(avgSystolic) : 0);
            stats.put("averageDiastolic", avgDiastolic != null ? Math.round(avgDiastolic) : 0);
            
            System.out.println("Service: Average BP: " + stats.get("averageSystolic") + "/" + stats.get("averageDiastolic"));
            
            // Calculate trend (compare with previous reading if available)
            if (allReadings.size() > 1) {
                BpData current = allReadings.get(0);
                BpData previous = allReadings.get(1);
                int currentMean = (current.getSystolic() + current.getDiastolic()) / 2;
                int previousMean = (previous.getSystolic() + previous.getDiastolic()) / 2;
                
                System.out.println("Service: Trend calculation - Current Mean: " + currentMean + ", Previous Mean: " + previousMean);
                
                if (currentMean > previousMean + 5) {
                    stats.put("trend", "up");
                    System.out.println("Service: Trend: UP");
                } else if (currentMean < previousMean - 5) {
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
            
            System.out.println("Service: Final BP stats for user " + userId + ": " + stats);
            
        } catch (Exception e) {
            System.err.println("Service: Error calculating BP stats for user " + userId + ":");
            e.printStackTrace();
            // Return default stats on error
            stats.put("currentSystolic", 0);
            stats.put("currentDiastolic", 0);
            stats.put("currentPulse", 0);
            stats.put("averageSystolic", 0);
            stats.put("averageDiastolic", 0);
            stats.put("trend", "stable");
        }
        
        return stats;
    }

    @Override
    public BpData getLatestBpReading(Long userId) {
        try {
            System.out.println("Service: Getting latest reading for user " + userId);
            List<BpData> readings = bpRepository.findByUserUserIdOrderByMeasuredAtDesc(userId);
            BpData latest = readings.isEmpty() ? null : readings.get(0);
            System.out.println("Service: Latest reading: " + (latest != null ? latest.getSystolic() + "/" + latest.getDiastolic() : "null"));
            return latest;
        } catch (Exception e) {
            System.err.println("Service: Error getting latest reading for user " + userId + ":");
            e.printStackTrace();
            return null;
        }
    }
}