package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.entity.AIAnalysis;

import java.time.Instant;
import java.util.List;

public record AIAnalysisResponse(
        String executiveSummary,
        String technicalAnalysis,
        List<String> riskFactors,
        List<String> recommendations,
        int confidence,
        String model,
        Instant generatedAt
) {
    public static AIAnalysisResponse from(AIAnalysis entity) {
        return new AIAnalysisResponse(
                entity.getExecutiveSummary(),
                entity.getTechnicalAnalysis(),
                entity.getRiskFactors(),
                entity.getRecommendations(),
                entity.getConfidence(),
                entity.getModel(),
                entity.getGeneratedAt()
        );
    }
}
