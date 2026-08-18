package com.harsh.sentinal.scan.controller;

import com.harsh.sentinal.scan.dto.ScanStatsResponse;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;
import com.harsh.sentinal.scan.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scan")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/stats")
    public ScanStatsResponse getStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "30") int days) {
        return reportService.getStats(userDetails, days);
    }
}
