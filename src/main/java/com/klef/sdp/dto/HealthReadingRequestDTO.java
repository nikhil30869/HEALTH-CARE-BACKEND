package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class HealthReadingRequestDTO {
    private Double temperature;
    private Double oxygenLevel;
    private Double glycogenLevel;
    private Integer systolic;
    private Integer diastolic;
    private Integer pulse;
    private Integer heartRate;
    private LocalDateTime measuredAt;
    private String notes;
    private Long userId;

    public HealthReadingRequestDTO() {}

    // Getters and Setters
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(Double oxygenLevel) { this.oxygenLevel = oxygenLevel; }

    public Double getGlycogenLevel() { return glycogenLevel; }
    public void setGlycogenLevel(Double glycogenLevel) { this.glycogenLevel = glycogenLevel; }

    public Integer getSystolic() { return systolic; }
    public void setSystolic(Integer systolic) { this.systolic = systolic; }

    public Integer getDiastolic() { return diastolic; }
    public void setDiastolic(Integer diastolic) { this.diastolic = diastolic; }

    public Integer getPulse() { return pulse; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }

    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}