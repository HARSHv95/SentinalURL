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
import java.util.UUID;

@Entity
@Table(name = "ai_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "scan_id", nullable = false)
    private UUID scanId;

    @Column(name = "executive_summary", columnDefinition = "text", nullable = false)
    private String executiveSummary;

    @Column(name = "technical_analysis", columnDefinition = "text", nullable = false)
    private String technicalAnalysis;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "risk_factors", columnDefinition = "jsonb", nullable = false)
    private List<String> riskFactors;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<String> recommendations;

    @Column(nullable = false)
    private int confidence;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(name = "generated_at", nullable = false)
    @CreationTimestamp
    private Instant generatedAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
