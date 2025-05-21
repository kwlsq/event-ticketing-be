package com.purwafest.purwafest.dashboard.application;

import com.purwafest.purwafest.dashboard.presentation.dtos.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboardData(String filter, Integer userId);
}
