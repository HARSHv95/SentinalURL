package com.harsh.sentinal.scan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnalysisResponse(
        Data data
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            String id,
            Attributes attributes
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Attributes(
            String status,
            Stats stats
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Stats(
            Integer harmless,
            Integer malicious,
            Integer suspicious,
            Integer undetected,
            Integer timeout
    ) {}
}
