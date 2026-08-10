package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<ProviderResultResponse> threatIntelligence;
    private DomainIntelligenceResponse domainIntelligence;
    private AIAnalysisResponse aiAnalysis;
}
