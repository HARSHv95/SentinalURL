package com.harsh.sentinal.scan.integration.googlesafebrowsing;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GoogleSafeBrowsingClient {

    private final RestClient restClient;
    private final GoogleSafeBrowsingProperties properties;

    public GoogleSafeBrowsingClient(RestClient.Builder builder, GoogleSafeBrowsingProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public GoogleSafeBrowsingResponse threatMatchesFind(String url) {
        GoogleSafeBrowsingResponse response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v4/threatMatches:find")
                        .queryParam("key", properties.getApiKey())
                        .build())
                .body(GoogleSafeBrowsingRequest.forUrl(url))
                .retrieve()
                .body(GoogleSafeBrowsingResponse.class);

        if (response == null) {
            throw new IllegalStateException("Failed to retrieve Google Safe Browsing response.");
        }

        return response;
    }
}
