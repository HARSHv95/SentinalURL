package com.harsh.sentinal.scan.service.threat;

import com.harsh.sentinal.scan.dto.RiskReport;

import java.util.List;

public record ThreatAggregationResult(
        List<ThreatProviderResult> providerResults,
        RiskReport riskReport
) {
}
