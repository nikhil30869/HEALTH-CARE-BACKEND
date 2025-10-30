package com.klef.sdp.service;

import com.klef.sdp.dto.DashboardDTO;

public interface DashboardService {
    DashboardDTO getDashboardData(Long userId);
}