package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class BpReadingDTO {
    private Long bpId; // ADD THIS FIELD
    private Integer systolic;
    private Integer diastolic;
    private Integer pulse;
    private LocalDateTime measuredAt;
    private String notes;
    private Long userId;

    public BpReadingDTO() {}

    // UPDATE CONSTRUCTOR
    public BpReadingDTO(Long bpId, Integer systolic, Integer diastolic, Integer pulse, LocalDateTime measuredAt, String notes, Long userId) {
        this.bpId = bpId;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.measuredAt = measuredAt;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getBpId() { return bpId; }
    public void setBpId(Long bpId) { this.bpId = bpId; }

    public Integer getSystolic() { return systolic; }
    public void setSystolic(Integer systolic) { this.systolic = systolic; }

    public Integer getDiastolic() { return diastolic; }
    public void setDiastolic(Integer diastolic) { this.diastolic = diastolic; }

    public Integer getPulse() { return pulse; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}