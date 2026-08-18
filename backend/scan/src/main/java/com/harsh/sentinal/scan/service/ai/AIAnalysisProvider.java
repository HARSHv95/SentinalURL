package com.harsh.sentinal.scan.service.ai;

import com.harsh.sentinal.scan.dto.AIAnalysisResult;
import com.harsh.sentinal.scan.dto.ThreatIntelligenceEvidence;

import java.util.Optional;

public interface AIAnalysisProvider {

    String getName();

    /**
     * Empty means unavailable/failed — never throws past this boundary, so a
     * caller can treat AI analysis as optional without try/catch of its own.
     */
    Optional<AIAnalysisResult> analyze(ThreatIntelligenceEvidence evidence);
}
