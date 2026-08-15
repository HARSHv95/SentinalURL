import type {
  RiskVerdict,
  ScanFilters,
  ScanSortOption,
  ScanStatus,
} from "../types/scan";

const SORT_OPTIONS: ScanSortOption[] = ["newest", "oldest", "risk_high", "risk_low"];
const STATUS_OPTIONS: ScanStatus[] = ["PENDING", "IN_PROGRESS", "COMPLETED", "FAILED"];
const VERDICT_OPTIONS: RiskVerdict[] = [
  "SAFE",
  "LOW_RISK",
  "MEDIUM_RISK",
  "HIGH_RISK",
  "CRITICAL",
];

export function parseScanFilters(params: URLSearchParams): ScanFilters {
  const page = Number(params.get("page"));
  const size = Number(params.get("size"));
  const sort = params.get("sort");
  const status = params.get("status");
  const verdict = params.get("verdict");
  const search = params.get("search");
  const emailScanBatchId = params.get("emailScanBatchId");

  return {
    page: Number.isFinite(page) && page >= 0 ? page : 0,
    size: Number.isFinite(size) && size > 0 ? size : 10,
    search: search ?? undefined,
    status: STATUS_OPTIONS.includes(status as ScanStatus)
      ? (status as ScanStatus)
      : undefined,
    verdict: VERDICT_OPTIONS.includes(verdict as RiskVerdict)
      ? (verdict as RiskVerdict)
      : undefined,
    sort: SORT_OPTIONS.includes(sort as ScanSortOption)
      ? (sort as ScanSortOption)
      : "newest",
    emailScanBatchId: emailScanBatchId ?? undefined,
  };
}
