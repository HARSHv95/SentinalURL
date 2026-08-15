package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanSummaryStats {
    private long totalScans;
    private long safeCount;
    private long suspiciousCount;
    private long maliciousCount;
    private long pendingCount;
}
