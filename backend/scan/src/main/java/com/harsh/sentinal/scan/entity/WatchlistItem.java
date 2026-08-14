package com.harsh.sentinal.scan.entity;

import com.harsh.sentinal.scan.common.enums.Verdict;
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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "watchlist_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(nullable = false)
    private String hostname;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_verdict", length = 30)
    private Verdict lastVerdict;

    @Column(name = "last_risk_score")
    private Integer lastRiskScore;

    @Column(name = "last_ssl_issuer", length = 500)
    private String lastSslIssuer;

    @Column(name = "last_ssl_valid_from")
    private Instant lastSslValidFrom;

    @Column(name = "last_ssl_valid_to")
    private Instant lastSslValidTo;

    @Column(name = "last_ssl_valid")
    private Boolean lastSslValid;

    @Column(name = "last_ssl_expiry_alerted_at")
    private Instant lastSslExpiryAlertedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "last_dns_records", columnDefinition = "jsonb")
    private Map<String, List<String>> lastDnsRecords;

    @Column(name = "last_registrar")
    private String lastRegistrar;

    @Column(name = "last_checked_at")
    private Instant lastCheckedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
