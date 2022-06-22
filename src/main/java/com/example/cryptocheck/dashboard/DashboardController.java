package com.example.cryptocheck.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashBoardInfo(
            Principal principal,
            @RequestParam(name="positionsAmount", required = false, defaultValue = "4") int positionsAmount) {
        var userEmail = principal.getName();
        var dashboard = dashboardService.getDashboardInfo(userEmail, positionsAmount);
        return ResponseEntity.ok(dashboard);
    }
}
