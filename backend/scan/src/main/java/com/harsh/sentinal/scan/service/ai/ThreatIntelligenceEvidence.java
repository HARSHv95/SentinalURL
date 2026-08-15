package com.harsh.sentinal.scan.service.ai;

import com.harsh.sentinal.scan.dto.RiskReport;
import com.harsh.sentinal.scan.entity.DomainIntelligence;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;

import java.util.List;

public record ThreatIntelligenceEvidence(
        String url,
        RiskReport riskReport,
        List<ThreatProviderResult> providerResults,
        DomainIntelligence domainIntelligence
) {
}
