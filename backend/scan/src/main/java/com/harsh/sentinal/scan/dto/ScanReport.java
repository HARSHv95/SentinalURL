package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanReport {
    private UUID scanId;
    private String url;
    private ScanStatus scanStatus;
    private int risk_score;
    private Instant created_at;
}
