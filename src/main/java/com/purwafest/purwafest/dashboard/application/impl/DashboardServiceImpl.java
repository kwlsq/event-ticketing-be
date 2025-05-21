package com.purwafest.purwafest.dashboard.application.impl;

import com.purwafest.purwafest.dashboard.application.DashboardService;
import com.purwafest.purwafest.dashboard.presentation.dtos.ChartData;
import com.purwafest.purwafest.dashboard.presentation.dtos.DashboardResponse;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceItemsRepository;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemsRepository invoiceItemsRepository;
    private final EventRepository eventRepository;

    public DashboardServiceImpl(InvoiceRepository invoiceRepository, InvoiceItemsRepository invoiceItemsRepository, EventRepository eventRepository){
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemsRepository = invoiceItemsRepository;
        this.eventRepository = eventRepository;
    }
    @Override
    public DashboardResponse getDashboardData(String filter, Integer userId){
        Long totalSales = invoiceRepository.sumFinalAmountByUserId(userId);

        Integer totalAttendees = invoiceItemsRepository.countByUserId(userId);

        Integer totalEvents = eventRepository.countEventByUserId(userId);

        List<ChartData> chartData;

        switch (filter.toLowerCase()) {
            case "daily":
                chartData = getTodayInvoices(userId);
                break;
            case "monthly":
                chartData = getCurrentMonthInvoices(userId);
                break;
            case "yearly":
                chartData = getCurrentYearInvoices(userId);
                break;
            default:
                throw new IllegalArgumentException("Invalid filter");
        }


        return DashboardResponse.builder()
                .totalSales(totalSales)
                .totalAttendees(totalAttendees)
                .totalEvents(totalEvents)
                .chartData(chartData)
                .build();
    }

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    private List<ChartData> getTodayInvoices(Integer userId) {
        LocalDate today = LocalDate.now();
        Instant start = today.atStartOfDay(ZONE_ID).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(ZONE_ID).toInstant().minusNanos(1);
        return invoiceRepository.findInvoicesByUserAndDateRange(userId, start, end);
    }

    private List<ChartData> getCurrentMonthInvoices(Integer userId) {
        YearMonth currentMonth = YearMonth.now();
        Instant start = currentMonth.atDay(1).atStartOfDay(ZONE_ID).toInstant();
        Instant end = currentMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
        return invoiceRepository.findInvoicesByUserAndDateRange(userId, start, end);
    }

    private List<ChartData> getCurrentYearInvoices(Integer userId) {
        int currentYear = Year.now().getValue();
        Instant start = LocalDate.of(currentYear, 1, 1).atStartOfDay(ZONE_ID).toInstant();
        Instant end = LocalDate.of(currentYear, 12, 31).atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
        return invoiceRepository.findInvoicesByUserAndDateRange(userId, start, end);
    }
}
