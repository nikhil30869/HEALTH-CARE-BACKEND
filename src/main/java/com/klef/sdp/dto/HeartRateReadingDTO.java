package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class HeartRateReadingDTO {
    private Long hrId; // ADD THIS FIELD
    private Integer heartRate;
    private String unit;
    private LocalDateTime measuredAt;
    private String notes;
    private Long userId;

    public HeartRateReadingDTO() {}

    // UPDATE CONSTRUCTOR
    public HeartRateReadingDTO(Long hrId, Integer heartRate, String unit, LocalDateTime measuredAt, String notes, Long userId) {
        this.hrId = hrId;
        this.heartRate = heartRate;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getHrId() { return hrId; }
    public void setHrId(Long hrId) { this.hrId = hrId; }

    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}