package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.AlertSeverity;
import com.harsh.sentinal.scan.common.enums.WatchlistAlertType;
import com.harsh.sentinal.scan.entity.WatchlistAlert;

import java.time.Instant;
import java.util.UUID;

public record WatchlistAlertResponse(
        UUID id,
        UUID watchlistItemId,
        String url,
        WatchlistAlertType type,
        AlertSeverity severity,
        String message,
        String previousValue,
        String newValue,
        boolean read,
        Instant createdAt
) {
    public static WatchlistAlertResponse from(WatchlistAlert entity) {
        return new WatchlistAlertResponse(
                entity.getId(),
                entity.getWatchlistItemId(),
                entity.getUrl(),
                entity.getType(),
                entity.getSeverity(),
                entity.getMessage(),
                entity.getPreviousValue(),
                entity.getNewValue(),
                entity.isRead(),
                entity.getCreatedAt()
        );
    }
}
