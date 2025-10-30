package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "heart_rate_data")
public class HeartRateData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "heart_rate", nullable = false)
    private Integer heartRate;

    @Column(name = "unit", nullable = false)
    private String unit = "bpm";

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "notes")
    private String notes;

    // Constructors, Getters and Setters
    public HeartRateData() {}

    public HeartRateData(User user, Integer heartRate, String unit, LocalDateTime measuredAt, String notes) {
        this.user = user;
        this.heartRate = heartRate;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getHrId() { return hrId; }
    public void setHrId(Long hrId) { this.hrId = hrId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}