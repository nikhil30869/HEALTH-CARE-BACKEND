package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "glycogen_data")
public class GlycogenData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glycogenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "glycogen_level", nullable = false)
    private Double glycogenLevel;

    @Column(name = "unit", nullable = false)
    private String unit = "mg/dL";

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "notes")
    private String notes;

    // Constructors
    public GlycogenData() {}

    public GlycogenData(User user, Double glycogenLevel, String unit, LocalDateTime measuredAt, String notes) {
        this.user = user;
        this.glycogenLevel = glycogenLevel;
        this.unit = unit;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getGlycogenId() { return glycogenId; }
    public void setGlycogenId(Long glycogenId) { this.glycogenId = glycogenId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getGlycogenLevel() { return glycogenLevel; }
    public void setGlycogenLevel(Double glycogenLevel) { this.glycogenLevel = glycogenLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}