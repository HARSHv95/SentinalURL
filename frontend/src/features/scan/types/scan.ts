export interface CreateScanRequest {
    url: string;
}

export interface CreateScanResponse {
    scanId: string;
    url: string;

    status: ScanStatus;

    createdAt: string;

    analysisReport: AnalysisReport | null;
    riskReport: RiskReport | null;
}

export type ScanStatus = "PENDING" | "IN_PROGRESS" | "COMPLETED" | "FAILED";

export type RiskVerdict =
  | "SAFE"
  | "LOW_RISK"
  | "MEDIUM_RISK"
  | "HIGH_RISK"
  | "CRITICAL";

export type ScanSortOption = "newest" | "oldest" | "risk_high" | "risk_low";

export interface ScanFilters {
  page: number;
  size: number;
  search?: string;
  status?: ScanStatus;
  verdict?: RiskVerdict;
  sort: ScanSortOption;
}

export interface AnalysisReport {
  status: string;
  malicious: number;
  harmless: number;
  suspicious: number;
  undetected: number;
}

export interface RiskReport {
  riskScore: number;
  verdict: RiskVerdict;
  confidence: number;
}

export interface ScanSummary {
  scanId: string;
  url: string;
  scanStatus: ScanStatus;
  riskScore: number;
  verdict: RiskVerdict | null;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
