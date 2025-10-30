package com.klef.sdp.service;

import com.klef.sdp.model.HeartRateData;
import com.klef.sdp.dto.HeartRateReadingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HeartRateService {
    HeartRateData addHeartRateReading(HeartRateData heartRateData);
    HeartRateData addHeartRateReading(Long userId, Integer heartRate, String unit, LocalDateTime measuredAt, String notes);
    List<HeartRateData> getUserHeartRateReadings(Long userId);
    List<HeartRateData> getUserHeartRateReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Map<String, Object> getHeartRateStats(Long userId);
    HeartRateData getLatestHeartRateReading(Long userId);
    
    // DTO METHODS
    List<HeartRateReadingDTO> getUserHeartRateReadingsDTO(Long userId);
    List<HeartRateReadingDTO> getUserHeartRateReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end);
}