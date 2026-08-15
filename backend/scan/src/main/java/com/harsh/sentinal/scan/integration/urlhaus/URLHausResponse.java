package com.harsh.sentinal.scan.integration.urlhaus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record URLHausResponse(
        String query_status,
        String threat,
        String date_added,
        List<String> tags
) {
}
