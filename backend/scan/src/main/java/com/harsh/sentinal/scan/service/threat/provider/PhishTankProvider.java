package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.integration.phishtank.PhishTankClient;
import com.harsh.sentinal.scan.integration.phishtank.PhishTankResponse;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhishTankProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "PHISHTANK";

    private final PhishTankClient phishTankClient;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        PhishTankResponse response = phishTankClient.checkUrl(url);
        PhishTankResponse.Results results = response.results();

        boolean malicious = results != null
                && results.in_database()
                && "y".equalsIgnoreCase(results.valid());

        String details = malicious
                ? "Listed in PhishTank as a verified phish (id " + results.phish_id() + ")"
                : "Not found in PhishTank as a verified phish";

        String reference = malicious ? results.phish_detail_page() : null;

        return new ThreatProviderResult(
                PROVIDER_NAME,
                ProviderStatus.AVAILABLE,
                malicious,
                false,
                null, null, null, null,
                List.of(),
                reference,
                Instant.now(),
                details
        );
    }
}
