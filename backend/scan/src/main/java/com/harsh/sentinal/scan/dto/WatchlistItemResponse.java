package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.entity.WatchlistItem;

import java.time.Instant;
import java.util.UUID;

public record WatchlistItemResponse(
        UUID id,
        String url,
        String hostname,
        Verdict lastVerdict,
        Integer lastRiskScore,
        Instant lastSslValidTo,
        Boolean lastSslValid,
        String lastRegistrar,
        Instant lastCheckedAt,
        Instant createdAt
) {
    public static WatchlistItemResponse from(WatchlistItem entity) {
        return new WatchlistItemResponse(
                entity.getId(),
                entity.getUrl(),
                entity.getHostname(),
                entity.getLastVerdict(),
                entity.getLastRiskScore(),
                entity.getLastSslValidTo(),
                entity.getLastSslValid(),
                entity.getLastRegistrar(),
                entity.getLastCheckedAt(),
                entity.getCreatedAt()
        );
    }
}
