package com.harsh.sentinal.scan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "analysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analysis {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private UUID scan_id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int malicious;

    @Column(nullable = false)
    private int harmless;

    @Column(nullable = false)
    private int suspicious;

    @Column(nullable = false)
    private int undetected;

    @CreationTimestamp
    private Instant created_at;
}
