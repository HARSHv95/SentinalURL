package com.harsh.sentinal.scan.integration.phishtank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PhishTankResponse(
        Results results
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Results(
            String url,
            boolean in_database,
            String phish_id,
            String verified,
            String valid,
            String phish_detail_page
    ) {}
}
