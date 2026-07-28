package com.harsh.sentinal.scan.util;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.AnalysisReport;
import com.harsh.sentinal.scan.dto.RiskReport;

public final class RiskScoreCalculator {

    private RiskScoreCalculator() {}

    public static RiskReport calculate(AnalysisReport report) {
        return calculate(
                report.getMalicious(),
                report.getHarmless(),
                report.getSuspicious(),
                report.getUndetected()
        );
    }

    public static RiskReport calculate(int malicious, int harmless, int suspicious, int undetected) {
        int score = Math.min((malicious * 20) + (suspicious * 10), 100);

        int totalEngines = malicious + harmless + suspicious + undetected;
        int confidence = totalEngines == 0
                ? 0
                : Math.round((malicious + suspicious) * 100f / totalEngines);

        return new RiskReport(score, verdictFor(score), confidence);
    }

    public static Verdict verdictFor(int score) {
        if (score <= 10) return Verdict.SAFE;
        if (score <= 30) return Verdict.LOW_RISK;
        if (score <= 60) return Verdict.MEDIUM_RISK;
        if (score <= 90) return Verdict.HIGH_RISK;
        return Verdict.CRITICAL;
    }
}
