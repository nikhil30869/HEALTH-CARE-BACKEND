package com.klef.sdp.service;

import com.klef.sdp.model.OxygenData;
import com.klef.sdp.dto.OxygenReadingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OxygenService {
    OxygenData addOxygenReading(OxygenData oxygenData);
    List<OxygenData> getUserOxygenReadings(Long userId);
    List<OxygenData> getUserOxygenReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Map<String, Object> getOxygenStats(Long userId);
    OxygenData getLatestOxygenReading(Long userId);
    
    // ADD DTO METHODS
    List<OxygenReadingDTO> getUserOxygenReadingsDTO(Long userId);
    List<OxygenReadingDTO> getUserOxygenReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end);
}