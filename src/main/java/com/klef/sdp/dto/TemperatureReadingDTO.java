package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class TemperatureReadingDTO {
    private Long tempId;
    private Long userId;
    private Double temperatureValue;
    private String unit;
    private LocalDateTime measuredAt;
    private String notes;

    // Constructors
    public TemperatureReadingDTO() {}

    public TemperatureReadingDTO(Long tempId, Long userId, Double temperatureValue, String unit, LocalDateTime measuredAt, String notes) {
        this.tempId = tempId;
        this.userId = userId;
        this.temperatureValue = temperatureValue;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getTempId() { return tempId; }
    public void setTempId(Long tempId) { this.tempId = tempId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getTemperatureValue() { return temperatureValue; }
    public void setTemperatureValue(Double temperatureValue) { this.temperatureValue = temperatureValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}