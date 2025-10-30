package com.klef.sdp.service;

import com.klef.sdp.model.BpData;
import com.klef.sdp.dto.BpReadingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BpService {
    BpData addBpReading(BpData bpData);
    List<BpData> getUserBpReadings(Long userId);
    List<BpData> getUserBpReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Map<String, Object> getBpStats(Long userId);
    BpData getLatestBpReading(Long userId);
    
    // ADD DTO METHODS
    List<BpReadingDTO> getUserBpReadingsDTO(Long userId);
    List<BpReadingDTO> getUserBpReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end);
}