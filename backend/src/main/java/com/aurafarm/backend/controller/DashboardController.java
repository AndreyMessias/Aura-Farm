package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.response.DashboardResponse;
import com.aurafarm.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aurafarm/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> obterDashboard() {
        return ResponseEntity.ok(dashboardService.obterDashboard());
    }
}
