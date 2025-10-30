package com.klef.sdp.service;

import com.klef.sdp.model.GlycogenData;
import com.klef.sdp.dto.GlycogenReadingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface GlycogenService {
    GlycogenData addGlycogenReading(GlycogenData glycogenData);
    List<GlycogenData> getUserGlycogenReadings(Long userId);
    List<GlycogenData> getUserGlycogenReadingsByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    Map<String, Object> getGlycogenStats(Long userId);
    GlycogenData getLatestGlycogenReading(Long userId);
    
    // DTO METHODS
    List<GlycogenReadingDTO> getUserGlycogenReadingsDTO(Long userId);
    List<GlycogenReadingDTO> getUserGlycogenReadingsByDateRangeDTO(Long userId, LocalDateTime start, LocalDateTime end);
}