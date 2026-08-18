package com.harsh.sentinal.scan.service.threat.provider;

import com.harsh.sentinal.scan.integration.urlhaus.URLHausClient;
import com.harsh.sentinal.scan.integration.urlhaus.URLHausProperties;
import com.harsh.sentinal.scan.integration.urlhaus.URLHausResponse;
import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.service.threat.ThreatProvider;
import com.harsh.sentinal.scan.dto.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class URLHausProvider implements ThreatProvider {

    private static final String PROVIDER_NAME = "URLHAUS";
    private static final String STATUS_LISTED = "ok";

    private final URLHausClient urlHausClient;
    private final URLHausProperties properties;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ThreatProviderResult analyze(String url) {
        if (properties.getAuthKey() == null || properties.getAuthKey().isBlank()) {
            return ThreatProviderResult.unavailable(PROVIDER_NAME, "URLHaus check skipped: no API key configured");
        }

        URLHausResponse response = urlHausClient.lookup(url);
        boolean malicious = STATUS_LISTED.equals(response.query_status());

        String details = malicious ? buildMaliciousDetails(response) : "Not found in URLhaus database";
        List<String> categories = malicious && response.threat() != null ? List.of(response.threat()) : List.of();

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

    private String buildMaliciousDetails(URLHausResponse response) {
        String tags = response.tags() != null && !response.tags().isEmpty()
                ? " (tags: " + String.join(", ", response.tags()) + ")"
                : "";

        return String.format(
                "Listed in URLhaus since %s — threat: %s%s",
                response.date_added(),
                response.threat(),
                tags
        );
    }
}
