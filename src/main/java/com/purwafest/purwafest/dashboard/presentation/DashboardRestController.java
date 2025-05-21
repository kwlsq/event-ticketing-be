package com.purwafest.purwafest.dashboard.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.dashboard.application.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardRestController {
    private final DashboardService dashboardService;

    public DashboardRestController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<?> getOrganizerDashboard(@RequestParam(defaultValue = "daily") String filter){
        Integer id= Claims.getUserId();
        return Response.successfulResponse(
                "Get " + filter + " data dashboard success",
                dashboardService.getDashboardData(filter,id)
        );
    }
}
