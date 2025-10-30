package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class ChartDataDTO {
    private LocalDateTime date;
    private Double value;
    private String label;

    public ChartDataDTO() {}

    public ChartDataDTO(LocalDateTime date, Double value) {
        this.date = date;
        this.value = value;
    }

    public ChartDataDTO(LocalDateTime date, Double value, String label) {
        this.date = date;
        this.value = value;
        this.label = label;
    }

    // Getters and Setters
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}