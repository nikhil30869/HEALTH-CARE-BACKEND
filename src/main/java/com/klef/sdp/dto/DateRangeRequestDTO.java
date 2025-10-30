package com.klef.sdp.dto;

import java.time.LocalDateTime;

public class DateRangeRequestDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long userId;

    public DateRangeRequestDTO() {}

    public DateRangeRequestDTO(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    // Getters and Setters
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}