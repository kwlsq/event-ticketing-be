package com.purwafest.purwafest.dashboard.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.dashboard.application.DashboardService;
import com.purwafest.purwafest.event.application.EventServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardRestController {
    private final DashboardService dashboardService;
    private final EventServices eventServices;

    public DashboardRestController(DashboardService dashboardService, EventServices eventServices){
        this.dashboardService = dashboardService;
        this.eventServices = eventServices;
    }

    @GetMapping
    public ResponseEntity<?> getOrganizerDashboard(@RequestParam(defaultValue = "daily") String filter){
        Integer id= Claims.getUserId();
        return Response.successfulResponse(
                "Get " + filter + " data dashboard success",
                dashboardService.getDashboardData(filter,id)
        );
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllOwnedEvents() {
        Integer id= Claims.getUserId();
        return Response.successfulResponse(
                "Fetched events successfully!",
                eventServices.getAllOwnedEvents(id)
        );
    }
}
