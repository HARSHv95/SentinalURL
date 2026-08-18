package com.harsh.sentinal.scan.dto;

import java.util.List;

public record ThreatAggregationResult(
        List<ThreatProviderResult> providerResults,
        RiskReport riskReport
) {
}
