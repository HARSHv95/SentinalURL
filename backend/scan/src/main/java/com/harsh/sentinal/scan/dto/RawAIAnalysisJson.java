package com.harsh.sentinal.scan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawAIAnalysisJson(
        String executiveSummary,
        String technicalAnalysis,
        List<String> riskFactors,
        List<String> recommendations,
        Integer confidence
) {
}
