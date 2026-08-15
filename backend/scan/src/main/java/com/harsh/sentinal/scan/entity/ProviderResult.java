package com.harsh.sentinal.scan.entity;

import com.harsh.sentinal.scan.common.enums.ProviderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "provider_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "scan_id", nullable = false)
    private UUID scanId;

    @Column(name = "provider_name", nullable = false, length = 30)
    private String providerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProviderStatus status;

    @Column(nullable = false)
    private boolean malicious;

    @Column(nullable = false)
    private boolean suspicious;

    @Column(name = "malicious_engine_count")
    private Integer maliciousEngineCount;

    @Column(name = "harmless_engine_count")
    private Integer harmlessEngineCount;

    @Column(name = "suspicious_engine_count")
    private Integer suspiciousEngineCount;

    @Column(name = "undetected_engine_count")
    private Integer undetectedEngineCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> categories;

    @Column(length = 500)
    private String reference;

    @Column(name = "checked_at")
    private Instant checkedAt;

    @Column(length = 1000)
    private String details;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;
}
