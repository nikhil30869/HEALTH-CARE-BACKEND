package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oxygen_data")
public class OxygenData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oxygenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "oxygen_level", nullable = false)
    private Double oxygenLevel;

    @Column(name = "unit", nullable = false)
    private String unit = "%";

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "notes")
    private String notes;

    // Constructors, Getters and Setters
    public OxygenData() {}

    public OxygenData(User user, Double oxygenLevel, String unit, LocalDateTime measuredAt, String notes) {
        this.user = user;
        this.oxygenLevel = oxygenLevel;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getOxygenId() { return oxygenId; }
    public void setOxygenId(Long oxygenId) { this.oxygenId = oxygenId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(Double oxygenLevel) { this.oxygenLevel = oxygenLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}