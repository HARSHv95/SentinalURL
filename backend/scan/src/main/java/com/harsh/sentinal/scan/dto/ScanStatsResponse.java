package com.harsh.sentinal.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanStatsResponse {
    private ScanSummaryStats summary;
    private List<VerdictCount> verdictDistribution;
    private List<DailyScanCount> dailyScanCounts;
    private List<RiskBucketCount> riskDistribution;
    private List<TopRiskyDomain> topRiskyDomains;
    private List<ScanReport> recentThreats;
}
