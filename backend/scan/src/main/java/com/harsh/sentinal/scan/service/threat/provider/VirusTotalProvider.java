package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.dto.AnalysisResponse;
import com.harsh.sentinal.scan.integration.virustotal.VirusTotalClient;
import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.dto.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VirusTotalProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "VIRUSTOTAL";
    private static final int MAX_POLL_ATTEMPTS = 60;
    private static final long POLL_INTERVAL_MS = 2000;

    private final VirusTotalClient virusTotalClient;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        String analysisId = virusTotalClient.submitUrl(url);

        AnalysisResponse response = pollUntilComplete(analysisId);
        AnalysisResponse.Stats stats = response.data().attributes().stats();

        boolean malicious = stats.malicious() > 0;
        boolean suspicious = stats.suspicious() > 0;

        String details = String.format(
                "VirusTotal: %d malicious, %d suspicious, %d harmless, %d undetected",
                stats.malicious(), stats.suspicious(), stats.harmless(), stats.undetected()
        );

        return new ThreatProviderResult(
                PROVIDER_NAME,
                ProviderStatus.AVAILABLE,
                malicious,
                suspicious,
                stats.malicious(),
                stats.harmless(),
                stats.suspicious(),
                stats.undetected(),
                List.of(),
                null,
                Instant.now(),
                details
        );
    }

    private AnalysisResponse pollUntilComplete(String analysisId) {
        for (int attempt = 0; attempt < MAX_POLL_ATTEMPTS; attempt++) {
            sleep();

            AnalysisResponse response = virusTotalClient.getAnalysis(analysisId);

            if ("completed".equals(response.data().attributes().status())) {
                return response;
            }
        }

        throw new IllegalStateException(
                "VirusTotal analysis did not complete within the allotted time: " + analysisId
        );
    }

    private void sleep() {
        try {
            Thread.sleep(POLL_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for VirusTotal analysis", e);
        }
    }
}
