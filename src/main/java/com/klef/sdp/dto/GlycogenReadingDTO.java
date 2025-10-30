package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class GlycogenReadingDTO {
    private Long glycogenId;
    private Double glycogenLevel;
    private String unit;
    private LocalDateTime measuredAt;
    private String notes;
    private Long userId;

    public GlycogenReadingDTO() {}

    public GlycogenReadingDTO(Long glycogenId, Double glycogenLevel, String unit, LocalDateTime measuredAt, String notes, Long userId) {
        this.glycogenId = glycogenId;
        this.glycogenLevel = glycogenLevel;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getGlycogenId() { return glycogenId; }
    public void setGlycogenId(Long glycogenId) { this.glycogenId = glycogenId; }

    public Double getGlycogenLevel() { return glycogenLevel; }
    public void setGlycogenLevel(Double glycogenLevel) { this.glycogenLevel = glycogenLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}