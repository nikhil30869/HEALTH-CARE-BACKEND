package com.klef.sdp.dto;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private int recordsThisWeek;
    private int parametersTracked;
    private int overallHealthScore;
    private List<Map<String, Object>> recentActivities;
    
    // Default constructor
    public DashboardDTO() {}
    
    // Parameterized constructor
    public DashboardDTO(int recordsThisWeek, int parametersTracked, int overallHealthScore, List<Map<String, Object>> recentActivities) {
        this.recordsThisWeek = recordsThisWeek;
        this.parametersTracked = parametersTracked;
        this.overallHealthScore = overallHealthScore;
        this.recentActivities = recentActivities;
    }
    
    // Getters and Setters
    public int getRecordsThisWeek() {
        return recordsThisWeek;
    }
    
    public void setRecordsThisWeek(int recordsThisWeek) {
        this.recordsThisWeek = recordsThisWeek;
    }
    
    public int getParametersTracked() {
        return parametersTracked;
    }
    
    public void setParametersTracked(int parametersTracked) {
        this.parametersTracked = parametersTracked;
    }
    
    public int getOverallHealthScore() {
        return overallHealthScore;
    }
    
    public void setOverallHealthScore(int overallHealthScore) {
        this.overallHealthScore = overallHealthScore;
    }
    
    public List<Map<String, Object>> getRecentActivities() {
        return recentActivities;
    }
    
    public void setRecentActivities(List<Map<String, Object>> recentActivities) {
        this.recentActivities = recentActivities;
    }
    
    // toString method
    @Override
    public String toString() {
        return "DashboardDTO{" +
                "recordsThisWeek=" + recordsThisWeek +
                ", parametersTracked=" + parametersTracked +
                ", overallHealthScore=" + overallHealthScore +
                ", recentActivities=" + recentActivities +
                '}';
    }
}