package com.klef.sdp.service;

import com.klef.sdp.model.TemperatureData;
import com.klef.sdp.dto.TemperatureReadingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TemperatureService {
    TemperatureData addTemperatureReading(TemperatureData temperatureData);
    List<TemperatureData> getUserTemperatureReadings(Long userId);
    List<TemperatureData> getUserTemperatureReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Map<String, Object> getTemperatureStats(Long userId);
    TemperatureData getLatestTemperatureReading(Long userId);
    
    // ADD THESE DTO METHODS:
    List<TemperatureReadingDTO> getUserTemperatureReadingsDTO(Long userId);
    List<TemperatureReadingDTO> getUserTemperatureReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end);
}