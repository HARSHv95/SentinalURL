package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.entity.EmailScanBatch;

import java.time.Instant;
import java.util.UUID;

public record EmailScanBatchResponse(
        UUID id,
        String subject,
        String senderPreview,
        int urlCount,
        Instant createdAt
) {
    public static EmailScanBatchResponse from(EmailScanBatch entity) {
        return new EmailScanBatchResponse(
                entity.getId(),
                entity.getSubject(),
                entity.getSenderPreview(),
                entity.getUrlCount(),
                entity.getCreatedAt()
        );
    }
}
