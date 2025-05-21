package com.purwafest.purwafest.dashboard.presentation.dtos;

import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalSales;
    private Integer totalAttendees;
    private Integer totalEvents;
    private List<ChartData> chartData;
}
