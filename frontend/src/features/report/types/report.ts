import type { RiskVerdict, ScanSummary } from "../../scan/types/scan";

export interface ScanSummaryStats {
  totalScans: number;
  safeCount: number;
  suspiciousCount: number;
  maliciousCount: number;
  pendingCount: number;
}

export interface VerdictCount {
  verdict: RiskVerdict;
  count: number;
}

export interface DailyScanCount {
  date: string;
  count: number;
}

export interface RiskBucketCount {
  bucket: string;
  count: number;
}

export interface TopRiskyDomain {
  domain: string;
  scanCount: number;
  avgRiskScore: number;
  maxRiskScore: number;
}

export interface ScanStatsResponse {
  summary: ScanSummaryStats;
  verdictDistribution: VerdictCount[];
  dailyScanCounts: DailyScanCount[];
  riskDistribution: RiskBucketCount[];
  topRiskyDomains: TopRiskyDomain[];
  recentThreats: ScanSummary[];
}
