package com.harsh.sentinal.scan.integration.abuseipdb;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AbuseIPDBClient {

    private static final int MAX_AGE_IN_DAYS = 30;

    private final RestClient restClient;
    private final AbuseIPDBProperties properties;

    public AbuseIPDBClient(RestClient.Builder builder, AbuseIPDBProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public AbuseIPDBResponse check(String ipAddress) {
        AbuseIPDBResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/check")
                        .queryParam("ipAddress", ipAddress)
                        .queryParam("maxAgeInDays", MAX_AGE_IN_DAYS)
                        .build())
                .header("Key", properties.getApiKey())
                .retrieve()
                .body(AbuseIPDBResponse.class);

        if (response == null) {
            throw new IllegalStateException("Failed to retrieve AbuseIPDB response.");
        }

        return response;
    }
}
