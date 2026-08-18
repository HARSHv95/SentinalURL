package com.harsh.sentinal.scan.service.discord;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.RiskReport;
import com.harsh.sentinal.scan.integration.discord.DiscordClient;
import com.harsh.sentinal.scan.integration.discord.DiscordEmbed;
import com.harsh.sentinal.scan.dto.ThreatAggregationResult;
import com.harsh.sentinal.scan.service.threat.ThreatAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordCommandService {

    private static final int COLOR_SAFE = 0x2ECC71;
    private static final int COLOR_WARNING = 0xF1C40F;
    private static final int COLOR_DANGER = 0xE74C3C;

    private final ThreatAggregationService threatAggregationService;
    private final DiscordClient discordClient;

    @Async
    public void handleScanCommand(String url, String interactionToken) {
        try {
            ThreatAggregationResult result = threatAggregationService.analyze(url);
            DiscordEmbed embed = buildResultEmbed(url, result);
            discordClient.sendFollowup(interactionToken, embed);
        } catch (Exception e) {
            log.warn("Discord /scan command failed for url {}", url, e);
            try {
                discordClient.sendFollowup(interactionToken, buildErrorEmbed());
            } catch (Exception followupFailure) {
                log.warn("Failed to send Discord error followup", followupFailure);
            }
        }
    }

    private DiscordEmbed buildResultEmbed(String url, ThreatAggregationResult result) {
        RiskReport riskReport = result.riskReport();
        Verdict verdict = riskReport.getVerdict();

        long flaggedCount = result.providerResults().stream()
                .filter(p -> p.malicious() || p.suspicious())
                .count();

        String flaggedSummary = flaggedCount == 0
                ? "No engines flagged this URL."
                : flaggedCount + " of " + result.providerResults().size() + " engines flagged this URL.";

        return new DiscordEmbed(
                "Scan result",
                url,
                colorForVerdict(verdict),
                List.of(
                        new DiscordEmbed.Field("Verdict", verdict.name(), true),
                        new DiscordEmbed.Field("Risk Score", riskReport.getRiskScore() + "/100", true),
                        new DiscordEmbed.Field("Engines", flaggedSummary, false)
                )
        );
    }

    private DiscordEmbed buildErrorEmbed() {
        return new DiscordEmbed(
                "Scan failed",
                "Something went wrong checking that URL. Please try again.",
                COLOR_DANGER,
                List.of()
        );
    }

    private int colorForVerdict(Verdict verdict) {
        return switch (verdict) {
            case SAFE -> COLOR_SAFE;
            case LOW_RISK, MEDIUM_RISK -> COLOR_WARNING;
            case HIGH_RISK, CRITICAL -> COLOR_DANGER;
        };
    }
}
