package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.entity.DomainIntelligence;

import java.util.List;

public record ThreatIntelligenceEvidence(
        String url,
        RiskReport riskReport,
        List<ThreatProviderResult> providerResults,
        DomainIntelligence domainIntelligence
) {
}
