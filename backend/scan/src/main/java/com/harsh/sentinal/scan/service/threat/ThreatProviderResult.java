package com.harsh.sentinal.scan.service.threat;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;

import java.time.Instant;
import java.util.List;

public record ThreatProviderResult(
        String providerName,
        ProviderStatus status,
        boolean malicious,
        boolean suspicious,
        Integer maliciousEngineCount,
        Integer harmlessEngineCount,
        Integer suspiciousEngineCount,
        Integer undetectedEngineCount,
        List<String> categories,
        String reference,
        Instant checkedAt,
        String details
) {

    public static ThreatProviderResult unavailable(String providerName, String reason) {
        return new ThreatProviderResult(
                providerName, ProviderStatus.UNAVAILABLE, false, false,
                null, null, null, null,
                List.of(), null, Instant.now(), reason
        );
    }

    public static ThreatProviderResult failed(String providerName, ProviderStatus status, String reason) {
        return new ThreatProviderResult(
                providerName, status, false, false,
                null, null, null, null,
                List.of(), null, Instant.now(), reason
        );
    }
}
