package com.harsh.sentinal.scan.service.ai;

import java.util.List;

public record AIAnalysisResult(
        String executiveSummary,
        String technicalAnalysis,
        List<String> riskFactors,
        List<String> recommendations,
        int confidence,
        String model
) {
}
