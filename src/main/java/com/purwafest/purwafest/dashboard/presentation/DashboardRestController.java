package com.purwafest.purwafest.dashboard.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardRestController {

    @GetMapping
    public ResponseEntity<?> getOrganizerDashboard(){
        return null;
    }
}
