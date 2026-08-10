package com.harsh.sentinal.scan.util;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.AnalysisReport;
import com.harsh.sentinal.scan.dto.RiskFactor;
import com.harsh.sentinal.scan.dto.RiskReport;
import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.service.threat.ThreatProviderResult;

import java.util.ArrayList;
import java.util.List;

public final class RiskScoreCalculator {

    private static final int ESCALATED_MIN_SCORE = 75;
    private static final int DOMAIN_AGE_THRESHOLD_DAYS = 30;
    private static final int DOMAIN_AGE_CONTRIBUTION = 15;

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
        int confidence = confidenceFor(malicious, harmless, suspicious, undetected);

        List<RiskFactor> factors = new ArrayList<>();
        if (malicious > 0) {
            factors.add(new RiskFactor(malicious + " engine(s) flagged this as malicious", malicious * 20));
        }
        if (suspicious > 0) {
            factors.add(new RiskFactor(suspicious + " engine(s) flagged this as suspicious", suspicious * 10));
        }

        return new RiskReport(score, verdictFor(score), confidence, factors);
    }

    /**
     * Confidence answers "how sure are we of this verdict", not "what fraction
     * flagged it malicious". A verdict is only as trustworthy as the fraction of
     * engines that actually agree with its direction — a risky verdict is only
     * confident if many engines flagged it; a safe verdict is only confident if
     * many engines actively confirmed it clean (engines with no opinion at all
     * don't count as evidence either way).
     */
    private static int confidenceFor(int malicious, int harmless, int suspicious, int undetected) {
        int total = malicious + harmless + suspicious + undetected;
        if (total == 0) {
            return 0;
        }

        int agreeing = (malicious + suspicious) > 0 ? (malicious + suspicious) : harmless;
        return Math.round(agreeing * 100f / total);
    }

    public static RiskReport calculate(List<ThreatProviderResult> results) {
        int malicious = 0;
        int harmless = 0;
        int suspicious = 0;
        int undetected = 0;
        int booleanProviderCount = 0;
        int booleanProviderMaliciousCount = 0;
        List<String> maliciousBooleanProviders = new ArrayList<>();

        for (ThreatProviderResult result : results) {
            if (result.status() != ProviderStatus.AVAILABLE) {
                continue;
            }

            if (result.maliciousEngineCount() != null) {
                malicious += result.maliciousEngineCount();
                harmless += nvl(result.harmlessEngineCount());
                suspicious += nvl(result.suspiciousEngineCount());
                undetected += nvl(result.undetectedEngineCount());
            } else {
                booleanProviderCount++;
                if (result.malicious()) {
                    booleanProviderMaliciousCount++;
                    maliciousBooleanProviders.add(result.providerName());
                }
            }
        }

        RiskReport base = calculate(malicious, harmless, suspicious, undetected);

        if (booleanProviderMaliciousCount > 0 && base.getVerdict().ordinal() < Verdict.HIGH_RISK.ordinal()) {
            int totalEngines = malicious + harmless + suspicious + undetected;
            int totalSignals = totalEngines + booleanProviderCount;
            int agreeingSignals = (malicious + suspicious) + booleanProviderMaliciousCount;

            int confidence = totalSignals == 0
                    ? 100
                    : Math.round(agreeingSignals * 100f / totalSignals);

            int escalatedScore = Math.max(base.getRiskScore(), ESCALATED_MIN_SCORE);
            int delta = escalatedScore - base.getRiskScore();

            List<RiskFactor> factors = new ArrayList<>(base.getFactors());
            if (delta > 0) {
                factors.add(new RiskFactor(
                        "Flagged by threat database(s): " + String.join(", ", maliciousBooleanProviders),
                        delta
                ));
            }

            return new RiskReport(escalatedScore, Verdict.HIGH_RISK, confidence, factors);
        }

        return base;
    }

    /**
     * Applies a small, fixed, documented adjustment when the scanned domain was
     * registered recently — a well-established phishing signal. Only fires when
     * Domain Intelligence actually resolved a creation date; never guesses.
     */
    public static RiskReport applyDomainAgeAdjustment(RiskReport base, Integer domainAgeDays) {
        if (domainAgeDays == null || domainAgeDays >= DOMAIN_AGE_THRESHOLD_DAYS) {
            return base;
        }

        int adjustedScore = Math.min(base.getRiskScore() + DOMAIN_AGE_CONTRIBUTION, 100);

        List<RiskFactor> factors = new ArrayList<>(base.getFactors());
        factors.add(new RiskFactor(
                "Domain registered " + domainAgeDays + " day(s) ago",
                DOMAIN_AGE_CONTRIBUTION
        ));

        Verdict adjustedVerdict = verdictFor(adjustedScore);
        Verdict finalVerdict = adjustedVerdict.ordinal() > base.getVerdict().ordinal() ? adjustedVerdict : base.getVerdict();

        return new RiskReport(adjustedScore, finalVerdict, base.getConfidence(), factors);
    }

    public static Verdict verdictFor(int score) {
        if (score <= 10) return Verdict.SAFE;
        if (score <= 30) return Verdict.LOW_RISK;
        if (score <= 60) return Verdict.MEDIUM_RISK;
        if (score <= 90) return Verdict.HIGH_RISK;
        return Verdict.CRITICAL;
    }

    private static int nvl(Integer value) {
        return value != null ? value : 0;
    }
}
