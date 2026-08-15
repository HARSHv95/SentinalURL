package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.common.enums.ScanStatus;
import com.harsh.sentinal.scan.common.enums.Verdict;
import com.harsh.sentinal.scan.entity.Scan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanReport {
    private UUID scanId;
    private String url;
    private ScanStatus scanStatus;
    private int riskScore;
    private Verdict verdict;
    private Instant createdAt;

    public static ScanReport from(Scan scan) {
        return new ScanReport(
                scan.getId(),
                scan.getUrl(),
                scan.getStatus(),
                scan.getRiskScore(),
                scan.getVerdict(),
                scan.getCreatedAt()
        );
    }
}
