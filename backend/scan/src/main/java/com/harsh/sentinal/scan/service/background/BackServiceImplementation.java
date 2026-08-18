package com.harsh.sentinal.scan.service.background;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.dto.RiskReport;
import com.harsh.sentinal.scan.entity.AIAnalysis;
import com.harsh.sentinal.scan.entity.Analysis;
import com.harsh.sentinal.scan.entity.DomainIntelligence;
import com.harsh.sentinal.scan.entity.ProviderResult;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.repository.AIAnalysisRepo;
import com.harsh.sentinal.scan.repository.AnalysisRepo;
import com.harsh.sentinal.scan.repository.ProviderResultRepo;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.service.ai.AIAnalysisProvider;
import com.harsh.sentinal.scan.dto.AIAnalysisResult;
import com.harsh.sentinal.scan.dto.ThreatIntelligenceEvidence;
import com.harsh.sentinal.scan.service.domain.DomainIntelligenceService;
import com.harsh.sentinal.scan.dto.ThreatAggregationResult;
import com.harsh.sentinal.scan.service.threat.ThreatAggregationService;
import com.harsh.sentinal.scan.dto.ThreatProviderResult;
import com.harsh.sentinal.scan.util.RiskScoreCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackServiceImplementation implements BackService {

    private final DomainIntelligenceService domainIntelligenceService;
    private final ThreatAggregationService threatAggregationService;
    private final AIAnalysisProvider aiAnalysisProvider;

    private final ScanRepo scanRepo;
    private final AnalysisRepo analysisRepo;
    private final ProviderResultRepo providerResultRepo;
    private final AIAnalysisRepo aiAnalysisRepo;

    @Override
    @Async
    public void processScan(UUID scanId, String url) {
        try {
            String hostname = extractHostname(url);

            DomainIntelligence domainIntel = hostname != null
                    ? safeDomainIntelligence(scanId, hostname)
                    : null;

            ThreatAggregationResult aggregation = threatAggregationService.analyze(url);

            if (aggregation.providerResults().isEmpty()) {
                throw new IllegalStateException("All threat providers failed for url: " + url);
            }

            Integer domainAgeDays = domainIntel != null ? domainIntel.getDomainAgeDays() : null;
            RiskReport riskReport = RiskScoreCalculator.applyDomainAgeAdjustment(aggregation.riskReport(), domainAgeDays);

            Scan scan = scanRepo.findById(scanId).orElseThrow();
            scan.setUpdatedAt(Instant.now());
            scan.setStatus(ScanStatus.COMPLETED);
            scan.setRiskScore(riskReport.getRiskScore());
            scan.setVerdict(riskReport.getVerdict());
            scanRepo.save(scan);

            aggregation.providerResults().forEach(providerResult ->
                    providerResultRepo.save(toProviderResultEntity(scanId, providerResult)));

            aggregation.providerResults().stream()
                    .filter(providerResult -> providerResult.maliciousEngineCount() != null)
                    .findFirst()
                    .ifPresent(virusTotalResult -> analysisRepo.save(toAnalysisEntity(scan, virusTotalResult)));

            generateAiAnalysis(scanId, url, riskReport, aggregation.providerResults(), domainIntel);

        } catch (Exception e) {
            log.warn("Scan {} failed", scanId, e);

            scanRepo.findById(scanId).ifPresent(scan -> {
                scan.setStatus(ScanStatus.FAILED);
                scanRepo.save(scan);
            });
        }
    }

    private DomainIntelligence safeDomainIntelligence(UUID scanId, String hostname) {
        try {
            return domainIntelligenceService.analyze(scanId, hostname);
        } catch (Exception e) {
            log.warn("Domain intelligence failed for {}", hostname, e);
            return null;
        }
    }

    private void generateAiAnalysis(
            UUID scanId, String url, RiskReport riskReport,
            List<ThreatProviderResult> providerResults, DomainIntelligence domainIntel) {

        try {
            ThreatIntelligenceEvidence evidence = new ThreatIntelligenceEvidence(url, riskReport, providerResults, domainIntel);
            Optional<AIAnalysisResult> aiResult = aiAnalysisProvider.analyze(evidence);
            aiResult.ifPresent(result -> aiAnalysisRepo.save(toAiAnalysisEntity(scanId, result)));
        } catch (Exception e) {
            // AI failure must never take down an otherwise-successful scan.
            log.warn("AI analysis failed for scan {}", scanId, e);
        }
    }

    private String extractHostname(String url) {
        try {
            return URI.create(url).getHost();
        } catch (Exception e) {
            return null;
        }
    }

    private ProviderResult toProviderResultEntity(UUID scanId, ThreatProviderResult result) {
        ProviderResult entity = new ProviderResult();

        entity.setScanId(scanId);
        entity.setProviderName(result.providerName());
        entity.setStatus(result.status());
        entity.setMalicious(result.malicious());
        entity.setSuspicious(result.suspicious());
        entity.setMaliciousEngineCount(result.maliciousEngineCount());
        entity.setHarmlessEngineCount(result.harmlessEngineCount());
        entity.setSuspiciousEngineCount(result.suspiciousEngineCount());
        entity.setUndetectedEngineCount(result.undetectedEngineCount());
        entity.setCategories(result.categories());
        entity.setReference(result.reference());
        entity.setCheckedAt(result.checkedAt());
        entity.setDetails(result.details());

        return entity;
    }

    private Analysis toAnalysisEntity(Scan scan, ThreatProviderResult virusTotalResult) {
        Analysis analysis = new Analysis();

        analysis.setId(UUID.randomUUID().toString());
        analysis.setScan_id(scan.getId());
        analysis.setUrl(scan.getUrl());
        analysis.setStatus("completed");
        analysis.setHarmless(virusTotalResult.harmlessEngineCount());
        analysis.setMalicious(virusTotalResult.maliciousEngineCount());
        analysis.setSuspicious(virusTotalResult.suspiciousEngineCount());
        analysis.setUndetected(virusTotalResult.undetectedEngineCount());

        return analysis;
    }

    private AIAnalysis toAiAnalysisEntity(UUID scanId, AIAnalysisResult result) {
        AIAnalysis entity = new AIAnalysis();

        entity.setScanId(scanId);
        entity.setExecutiveSummary(result.executiveSummary());
        entity.setTechnicalAnalysis(result.technicalAnalysis());
        entity.setRiskFactors(result.riskFactors());
        entity.setRecommendations(result.recommendations());
        entity.setConfidence(result.confidence());
        entity.setModel(result.model());

        return entity;
    }
}
