package com.harsh.sentinal.scan.entity;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.ShareVisibility;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.dto.AnalysisResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanStatus status;

    @Column(name = "risk_score", nullable = true)
    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 30)
    private Verdict verdict;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    private Instant updatedAt;

    @Column(name = "email_scan_batch_id")
    private UUID emailScanBatchId;

    @Column(name = "share_token", unique = true)
    private String shareToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_visibility", nullable = false, length = 20)
    private ShareVisibility shareVisibility = ShareVisibility.PRIVATE;

}
