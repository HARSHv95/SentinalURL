package com.harsh.sentinal.scan.service.background.Implementation;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.dto.AnalysisResponse;
import com.harsh.sentinal.scan.dto.RiskReport;
import com.harsh.sentinal.scan.entity.Analysis;
import com.harsh.sentinal.scan.entity.Scan;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.repository.AnalysisRepo;
import com.harsh.sentinal.scan.repository.ScanRepo;
import com.harsh.sentinal.scan.service.background.BackService;
import com.harsh.sentinal.scan.util.RiskScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BackServiceImplementation implements BackService {

    private final VirusTotalClient virusTotalClient;

    @Autowired
    private ScanRepo scanRepo;

    @Autowired
    private AnalysisRepo analysisRepo;


    @Override
    @Async
    public void processScan(UUID scanId, String url) {
        try {

            String analysisId = virusTotalClient.submitUrl(url);

            AnalysisResponse response;

            do {

                Thread.sleep(2000);

                response = virusTotalClient.getAnalysis(analysisId);

            } while (!response.data().attributes().status().equals("completed"));

            Scan scan = scanRepo.findById(scanId)
                    .orElseThrow();

            scan.setUpdatedAt(Instant.now());
            scan.setStatus(ScanStatus.COMPLETED);

            AnalysisResponse.Stats stats = response.data().attributes().stats();

            RiskReport riskReport = RiskScoreCalculator.calculate(
                    stats.malicious(),
                    stats.harmless(),
                    stats.suspicious(),
                    stats.undetected()
            );

            scan.setRiskScore(riskReport.getRiskScore());
            scan.setVerdict(riskReport.getVerdict());

            Analysis analysis = new Analysis();

            analysis.setId(response.data().id());
            analysis.setScan_id(scan.getId());
            analysis.setUrl(scan.getUrl());
            analysis.setStatus(response.data().attributes().status());
            analysis.setHarmless(response.data().attributes().stats().harmless());
            analysis.setMalicious(response.data().attributes().stats().malicious());
            analysis.setSuspicious(response.data().attributes().stats().suspicious());
            analysis.setUndetected(response.data().attributes().stats().undetected());

            scanRepo.save(scan);
            analysisRepo.save(analysis);

        } catch (Exception e) {

            Scan scan = scanRepo.findById(scanId)
                    .orElseThrow();

            scan.setStatus(ScanStatus.FAILED);

            scanRepo.save(scan);
        }
    }
}
