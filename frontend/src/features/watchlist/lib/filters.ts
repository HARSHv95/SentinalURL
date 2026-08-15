import type { RiskVerdict, WatchlistFilters, WatchlistSortOption } from "../types/watchlist";

const SORT_OPTIONS: WatchlistSortOption[] = ["newest", "oldest", "risk_high", "risk_low"];
const VERDICT_OPTIONS: RiskVerdict[] = [
  "SAFE",
  "LOW_RISK",
  "MEDIUM_RISK",
  "HIGH_RISK",
  "CRITICAL",
];

export function parseWatchlistFilters(params: URLSearchParams): WatchlistFilters {
  const page = Number(params.get("page"));
  const size = Number(params.get("size"));
  const sort = params.get("sort");
  const verdict = params.get("verdict");
  const search = params.get("search");

  return {
    page: Number.isFinite(page) && page >= 0 ? page : 0,
    size: Number.isFinite(size) && size > 0 ? size : 10,
    search: search ?? undefined,
    verdict: VERDICT_OPTIONS.includes(verdict as RiskVerdict)
      ? (verdict as RiskVerdict)
      : undefined,
    sort: SORT_OPTIONS.includes(sort as WatchlistSortOption)
      ? (sort as WatchlistSortOption)
      : "newest",
  };
}
