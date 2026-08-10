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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "domain_intelligence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainIntelligence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "scan_id", nullable = false)
    private UUID scanId;

    @Column(nullable = false)
    private String domain;

    private String registrar;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(name = "domain_age_days")
    private Integer domainAgeDays;

    private String country;

    @Column(name = "hosting_provider")
    private String hostingProvider;

    private String asn;

    @Column(name = "ip_addresses")
    private String ipAddresses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dns_records", columnDefinition = "jsonb")
    private Map<String, List<String>> dnsRecords;

    @Column(name = "ssl_issuer")
    private String sslIssuer;

    @Column(name = "ssl_valid_from")
    private Instant sslValidFrom;

    @Column(name = "ssl_valid_to")
    private Instant sslValidTo;

    @Column(name = "ssl_valid")
    private Boolean sslValid;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
