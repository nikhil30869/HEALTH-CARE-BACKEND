package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "temperature_data")
public class TemperatureData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "temperature_value", nullable = false)
    private Double temperatureValue;

    @Column(name = "unit", nullable = false)
    private String unit = "°F";

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "notes")
    private String notes;

    // Constructors
    public TemperatureData() {}

    public TemperatureData(User user, Double temperatureValue, String unit, LocalDateTime measuredAt, String notes) {
        this.user = user;
        this.temperatureValue = temperatureValue;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getTempId() { return tempId; }
    public void setTempId(Long tempId) { this.tempId = tempId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getTemperatureValue() { return temperatureValue; }
    public void setTemperatureValue(Double temperatureValue) { this.temperatureValue = temperatureValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}