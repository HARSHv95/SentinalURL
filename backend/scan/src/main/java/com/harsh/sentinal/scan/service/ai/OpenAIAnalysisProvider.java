package com.harsh.sentinal.scan.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsh.sentinal.scan.dto.AIAnalysisResult;
import com.harsh.sentinal.scan.dto.RawAIAnalysisJson;
import com.harsh.sentinal.scan.dto.RiskFactor;
import com.harsh.sentinal.scan.dto.ThreatIntelligenceEvidence;
import com.harsh.sentinal.scan.entity.DomainIntelligence;
import com.harsh.sentinal.scan.integration.openai.OpenAIClient;
import com.harsh.sentinal.scan.integration.openai.OpenAIProperties;
import com.harsh.sentinal.scan.dto.ThreatProviderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIAnalysisProvider implements AIAnalysisProvider {

    private static final String PROVIDER_NAME = "OPENAI";
    private static final String UNAVAILABLE = "unavailable";

    private static final String SYSTEM_PROMPT = """
            You are a cybersecurity analyst producing a threat assessment for a URL \
            security scanning platform. You are given structured evidence gathered from \
            multiple threat intelligence sources and domain intelligence lookups.

            Separate your reasoning into three categories:
            - FACTS: only what is explicitly present in the evidence given to you. Any \
            field marked "unavailable" means that data could not be collected — you must \
            say it is unavailable, never guess or invent a value for it.
            - INTERPRETATION: your analysis of what the facts suggest, clearly derived \
            from the facts given, not from outside knowledge.
            - RECOMMENDATIONS: actionable guidance for a user who encountered this URL.

            You must NEVER invent: provider detections, domain age, IP addresses, \
            registrar names, DNS records, SSL certificate details, malware family names, \
            phishing campaign names, or threat actor names that are not explicitly present \
            in the evidence below.

            Respond ONLY with a JSON object matching exactly this schema, no other text:
            {
              "executiveSummary": string (2-4 sentences, plain language, based only on the evidence),
              "technicalAnalysis": string (a paragraph explaining the technical findings from the evidence),
              "riskFactors": string[] (specific factors drawn from the evidence; empty array if none),
              "recommendations": string[] (actionable guidance for the user),
              "confidence": number (0-100, your confidence in this assessment given how complete the evidence is)
            }""";

    private final OpenAIClient openAIClient;
    private final OpenAIProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public Optional<AIAnalysisResult> analyze(ThreatIntelligenceEvidence evidence) {
        if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            log.info("AI analysis skipped: no OpenAI API key configured");
            return Optional.empty();
        }

        try {
            String userPrompt = buildUserPrompt(evidence);
            String rawJson = openAIClient.chatCompletion(SYSTEM_PROMPT, userPrompt);

            RawAIAnalysisJson parsed = objectMapper.readValue(rawJson, RawAIAnalysisJson.class);

            return validate(parsed);
        } catch (Exception e) {
            log.warn("AI analysis failed", e);
            return Optional.empty();
        }
    }

    private Optional<AIAnalysisResult> validate(RawAIAnalysisJson raw) {
        if (raw.executiveSummary() == null || raw.executiveSummary().isBlank()) {
            return Optional.empty();
        }
        if (raw.technicalAnalysis() == null || raw.technicalAnalysis().isBlank()) {
            return Optional.empty();
        }
        if (raw.riskFactors() == null || raw.recommendations() == null) {
            return Optional.empty();
        }
        if (raw.confidence() == null || raw.confidence() < 0 || raw.confidence() > 100) {
            return Optional.empty();
        }

        return Optional.of(new AIAnalysisResult(
                raw.executiveSummary(),
                raw.technicalAnalysis(),
                raw.riskFactors(),
                raw.recommendations(),
                raw.confidence(),
                properties.getModel()
        ));
    }

    private String buildUserPrompt(ThreatIntelligenceEvidence evidence) {
        StringBuilder sb = new StringBuilder();

        sb.append("URL: ").append(evidence.url()).append("\n\n");

        sb.append("RISK ASSESSMENT (computed deterministically, not by you):\n");
        sb.append("- Score: ").append(evidence.riskReport().getRiskScore()).append("/100\n");
        sb.append("- Verdict: ").append(evidence.riskReport().getVerdict()).append("\n");
        sb.append("- Confidence: ").append(evidence.riskReport().getConfidence()).append("%\n");
        for (RiskFactor factor : evidence.riskReport().getFactors()) {
            sb.append("  - Factor: ").append(factor.label()).append(" (+").append(factor.contribution()).append(")\n");
        }

        sb.append("\nTHREAT INTELLIGENCE PROVIDERS:\n");
        for (ThreatProviderResult result : evidence.providerResults()) {
            sb.append("- ").append(result.providerName())
                    .append(" [").append(result.status()).append("]: ");

            if (result.status().name().equals("AVAILABLE")) {
                sb.append(result.malicious() ? "MALICIOUS" : "CLEAN")
                        .append(" — ").append(orUnavailable(result.details()));
                if (!result.categories().isEmpty()) {
                    sb.append(" (categories: ").append(String.join(", ", result.categories())).append(")");
                }
            } else {
                sb.append(orUnavailable(result.details()));
            }
            sb.append("\n");
        }

        sb.append("\nDOMAIN INTELLIGENCE:\n");
        DomainIntelligence domain = evidence.domainIntelligence();
        if (domain == null) {
            sb.append("- ").append(UNAVAILABLE).append("\n");
        } else {
            sb.append("- Domain: ").append(orUnavailable(domain.getDomain())).append("\n");
            sb.append("- Registrar: ").append(orUnavailable(domain.getRegistrar())).append("\n");
            sb.append("- Creation date: ").append(orUnavailable(domain.getCreationDate())).append("\n");
            sb.append("- Domain age (days): ").append(orUnavailable(domain.getDomainAgeDays())).append("\n");
            sb.append("- Country: ").append(orUnavailable(domain.getCountry())).append("\n");
            sb.append("- Hosting provider: ").append(orUnavailable(domain.getHostingProvider())).append("\n");
            sb.append("- ASN: ").append(orUnavailable(domain.getAsn())).append("\n");
            sb.append("- IP addresses: ").append(orUnavailable(domain.getIpAddresses())).append("\n");
            sb.append("- DNS records: ").append(orUnavailable(domain.getDnsRecords())).append("\n");
            sb.append("- SSL issuer: ").append(orUnavailable(domain.getSslIssuer())).append("\n");
            sb.append("- SSL valid: ").append(orUnavailable(domain.getSslValid())).append("\n");
        }

        return sb.toString();
    }

    private String orUnavailable(Object value) {
        if (value == null) {
            return UNAVAILABLE;
        }
        if (value instanceof String s && s.isBlank()) {
            return UNAVAILABLE;
        }
        if (value instanceof List<?> l && l.isEmpty()) {
            return UNAVAILABLE;
        }
        return value.toString();
    }
}
