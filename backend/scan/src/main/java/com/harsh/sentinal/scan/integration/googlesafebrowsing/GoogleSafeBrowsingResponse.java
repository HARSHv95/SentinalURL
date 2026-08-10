package com.harsh.sentinal.scan.integration.googlesafebrowsing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleSafeBrowsingResponse(
        List<Match> matches
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Match(String threatType, String platformType, String threatEntryType) {}
}
