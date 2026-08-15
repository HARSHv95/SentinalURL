package com.harsh.sentinal.scan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_scan_batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailScanBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(length = 500)
    private String subject;

    @Column(name = "sender_preview", length = 500)
    private String senderPreview;

    @Column(name = "source_message_id", nullable = false)
    private String sourceMessageId;

    @Column(name = "url_count", nullable = false)
    private int urlCount;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;
}
