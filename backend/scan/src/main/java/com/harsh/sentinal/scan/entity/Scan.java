package com.harsh.sentinal.scan.entity;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
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

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanStatus status;

    @Column(nullable = true)
    private int risk_score;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant created_at;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant updated_at;

}
