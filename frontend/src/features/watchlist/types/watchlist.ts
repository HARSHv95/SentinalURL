export type RiskVerdict =
  | "SAFE"
  | "LOW_RISK"
  | "MEDIUM_RISK"
  | "HIGH_RISK"
  | "CRITICAL";

export type WatchlistSortOption = "newest" | "oldest" | "risk_high" | "risk_low";

export type WatchlistAlertType =
  | "SSL_EXPIRING"
  | "SSL_CHANGED"
  | "VERDICT_CHANGED"
  | "DNS_CHANGED"
  | "WHOIS_CHANGED";

export type AlertSeverity = "INFO" | "WARNING" | "CRITICAL";

export interface WatchlistItem {
  id: string;
  url: string;
  hostname: string;
  lastVerdict: RiskVerdict | null;
  lastRiskScore: number | null;
  lastSslValidTo: string | null;
  lastSslValid: boolean | null;
  lastRegistrar: string | null;
  lastCheckedAt: string | null;
  createdAt: string;
}

export interface WatchlistAlert {
  id: string;
  watchlistItemId: string;
  url: string;
  type: WatchlistAlertType;
  severity: AlertSeverity;
  message: string;
  previousValue: string | null;
  newValue: string | null;
  read: boolean;
  createdAt: string;
}

export interface WatchlistFilters {
  page: number;
  size: number;
  search?: string;
  verdict?: RiskVerdict;
  sort: WatchlistSortOption;
}

export interface WatchlistAlertFilters {
  page: number;
  size: number;
  unreadOnly?: boolean;
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
