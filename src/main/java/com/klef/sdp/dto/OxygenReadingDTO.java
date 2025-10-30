package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class OxygenReadingDTO {
    private Long oxygenId; // ADD THIS FIELD
    private Double oxygenLevel;
    private String unit;
    private LocalDateTime measuredAt;
    private String notes;
    private Long userId;

    public OxygenReadingDTO() {}

    // UPDATE CONSTRUCTOR
    public OxygenReadingDTO(Long oxygenId, Double oxygenLevel, String unit, LocalDateTime measuredAt, String notes, Long userId) {
        this.oxygenId = oxygenId;
        this.oxygenLevel = oxygenLevel;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getOxygenId() { return oxygenId; }
    public void setOxygenId(Long oxygenId) { this.oxygenId = oxygenId; }

    public Double getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(Double oxygenLevel) { this.oxygenLevel = oxygenLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}