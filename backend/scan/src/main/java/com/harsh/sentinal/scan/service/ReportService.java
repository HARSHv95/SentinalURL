package com.harsh.sentinal.scan.service;

import com.harsh.sentinal.scan.dto.ScanStatsResponse;
import com.harsh.sentinal.scan.security.principal.CustomUserDetails;

public interface ReportService {
    ScanStatsResponse getStats(CustomUserDetails userDetails, int days);
}
