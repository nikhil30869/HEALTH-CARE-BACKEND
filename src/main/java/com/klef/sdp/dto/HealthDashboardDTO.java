package com.klef.sdp.dto;

import com.klef.sdp.model.TemperatureData;
import com.klef.sdp.model.OxygenData;
import com.klef.sdp.model.GlycogenData;
import com.klef.sdp.model.BpData;
import com.klef.sdp.model.HeartRateData;
import java.util.Map;

public class HealthDashboardDTO {
    private TemperatureData latestTemperature;
    private OxygenData latestOxygen;
    private GlycogenData latestGlycogen;
    private BpData latestBloodPressure;
    private HeartRateData latestHeartRate;
    private Map<String, Object> temperatureStats;
    private Map<String, Object> oxygenStats;
    private Map<String, Object> glycogenStats;
    private Map<String, Object> bpStats;
    private Map<String, Object> heartRateStats;

    public HealthDashboardDTO() {}

    // Getters and Setters
    public TemperatureData getLatestTemperature() { return latestTemperature; }
    public void setLatestTemperature(TemperatureData latestTemperature) { this.latestTemperature = latestTemperature; }

    public OxygenData getLatestOxygen() { return latestOxygen; }
    public void setLatestOxygen(OxygenData latestOxygen) { this.latestOxygen = latestOxygen; }

    public GlycogenData getLatestGlycogen() { return latestGlycogen; }
    public void setLatestGlycogen(GlycogenData latestGlycogen) { this.latestGlycogen = latestGlycogen; }

    public BpData getLatestBloodPressure() { return latestBloodPressure; }
    public void setLatestBloodPressure(BpData latestBloodPressure) { this.latestBloodPressure = latestBloodPressure; }

    public HeartRateData getLatestHeartRate() { return latestHeartRate; }
    public void setLatestHeartRate(HeartRateData latestHeartRate) { this.latestHeartRate = latestHeartRate; }

    public Map<String, Object> getTemperatureStats() { return temperatureStats; }
    public void setTemperatureStats(Map<String, Object> temperatureStats) { this.temperatureStats = temperatureStats; }

    public Map<String, Object> getOxygenStats() { return oxygenStats; }
    public void setOxygenStats(Map<String, Object> oxygenStats) { this.oxygenStats = oxygenStats; }

    public Map<String, Object> getGlycogenStats() { return glycogenStats; }
    public void setGlycogenStats(Map<String, Object> glycogenStats) { this.glycogenStats = glycogenStats; }

    public Map<String, Object> getBpStats() { return bpStats; }
    public void setBpStats(Map<String, Object> bpStats) { this.bpStats = bpStats; }

    public Map<String, Object> getHeartRateStats() { return heartRateStats; }
    public void setHeartRateStats(Map<String, Object> heartRateStats) { this.heartRateStats = heartRateStats; }
}