package com.harsh.sentinal.scan.integration.abuseipdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AbuseIPDBResponse(
        Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            String ipAddress,
            int abuseConfidenceScore,
            String countryCode,
            String isp,
            String domain,
            int totalReports
    ) {}
}
