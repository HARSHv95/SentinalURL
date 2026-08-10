package com.harsh.sentinal.scan.integration.geoip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * ip-api.com's free JSON endpoint — no API key, rate-limited to 45 req/min
 * for non-commercial use. Fixed, hardcoded base URL since nothing about it
 * needs per-environment configuration.
 */
@Service
public class IpGeoClient {

    private final RestClient restClient;

    public IpGeoClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://ip-api.com").build();
    }

    public IpGeoResponse lookup(String ip) {
        try {
            return restClient.get()
                    .uri("/json/{ip}?fields=status,country,countryCode,isp,org,as", ip)
                    .retrieve()
                    .body(IpGeoResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IpGeoResponse(
            String status,
            String country,
            String countryCode,
            String isp,
            String org,
            String as
    ) {}
}
