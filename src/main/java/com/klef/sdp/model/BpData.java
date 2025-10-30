package com.klef.sdp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bp_data")
public class BpData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bpId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "systolic", nullable = false)
    private Integer systolic;

    @Column(name = "diastolic", nullable = false)
    private Integer diastolic;

    @Column(name = "pulse", nullable = false)
    private Integer pulse;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "notes")
    private String notes;

    // Constructors, Getters and Setters
    public BpData() {}

    public BpData(User user, Integer systolic, Integer diastolic, Integer pulse, LocalDateTime measuredAt, String notes) {
        this.user = user;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.measuredAt = measuredAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getBpId() { return bpId; }
    public void setBpId(Long bpId) { this.bpId = bpId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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
}