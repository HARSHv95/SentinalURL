package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.integration.googlesafebrowsing.GoogleSafeBrowsingClient;
import com.harsh.sentinal.scan.integration.googlesafebrowsing.GoogleSafeBrowsingProperties;
import com.harsh.sentinal.scan.integration.googlesafebrowsing.GoogleSafeBrowsingResponse;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleSafeBrowsingProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "GOOGLE_SAFE_BROWSING";

    private final GoogleSafeBrowsingClient client;
    private final GoogleSafeBrowsingProperties properties;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            return ThreatProviderResult.unavailable(PROVIDER_NAME, "Google Safe Browsing check skipped: no API key configured");
        }

        GoogleSafeBrowsingResponse response = client.threatMatchesFind(url);
        boolean malicious = response.matches() != null && !response.matches().isEmpty();

        List<String> categories = malicious
                ? response.matches().stream().map(GoogleSafeBrowsingResponse.Match::threatType).distinct().collect(Collectors.toList())
                : List.of();

        String details = malicious
                ? "Flagged by Google Safe Browsing: " + String.join(", ", categories)
                : "Not flagged by Google Safe Browsing";

        return new ThreatProviderResult(
                PROVIDER_NAME,
                ProviderStatus.AVAILABLE,
                malicious,
                false,
                null, null, null, null,
                categories,
                null,
                Instant.now(),
                details
        );
    }
}
