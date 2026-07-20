package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanResponse {
    private UUID scanId;
    private String url;
    private String status;
    private LocalDateTime createdAt;
    private AnalysisReport analysisReport;
    private RiskReport riskReport;
}
