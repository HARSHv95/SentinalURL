package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import com.harsh.sentinal.scan.entity.ProviderResult;

import java.time.Instant;
import java.util.List;

public record ProviderResultResponse(
        String providerName,
        ProviderStatus status,
        boolean malicious,
        boolean suspicious,
        Integer maliciousEngineCount,
        Integer harmlessEngineCount,
        Integer suspiciousEngineCount,
        Integer undetectedEngineCount,
        List<String> categories,
        String reference,
        Instant checkedAt,
        String details
) {
    public static ProviderResultResponse from(ProviderResult entity) {
        return new ProviderResultResponse(
                entity.getProviderName(),
                entity.getStatus(),
                entity.isMalicious(),
                entity.isSuspicious(),
                entity.getMaliciousEngineCount(),
                entity.getHarmlessEngineCount(),
                entity.getSuspiciousEngineCount(),
                entity.getUndetectedEngineCount(),
                entity.getCategories(),
                entity.getReference(),
                entity.getCheckedAt(),
                entity.getDetails()
        );
    }
}
