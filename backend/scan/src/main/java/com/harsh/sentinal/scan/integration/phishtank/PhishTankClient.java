package com.harsh.sentinal.scan.integration.phishtank;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PhishTankClient {

    private static final String USER_AGENT = "phishtank/sentinalurl";

    private final RestClient restClient;
    private final PhishTankProperties properties;

    public PhishTankClient(RestClient.Builder builder, PhishTankProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("User-Agent", USER_AGENT)
                .build();
    }

    public PhishTankResponse checkUrl(String url) {
        StringBuilder body = new StringBuilder()
                .append("url=").append(URLEncoder.encode(url, StandardCharsets.UTF_8))
                .append("&format=json");

        if (properties.getAppKey() != null && !properties.getAppKey().isBlank()) {
            body.append("&app_key=").append(URLEncoder.encode(properties.getAppKey(), StandardCharsets.UTF_8));
        }

        PhishTankResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body.toString())
                .retrieve()
                .body(PhishTankResponse.class);

        if (response == null) {
            throw new IllegalStateException("Failed to retrieve PhishTank response.");
        }

        return response;
    }
}
