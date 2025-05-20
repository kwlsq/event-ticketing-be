package com.purwafest.purwafest.dashboard.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalSales;
    private Integer totalAttendees;
    private Integer totalEvents;
    private List<ChartData> chartData;
}
