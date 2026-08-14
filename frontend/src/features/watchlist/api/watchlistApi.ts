import { scanClient } from "../../../shared/api/apiClient";

import type {
  PageResponse,
  WatchlistAlert,
  WatchlistAlertFilters,
  WatchlistFilters,
  WatchlistItem,
} from "../types/watchlist";

export async function addWatchlistItem(url: string): Promise<WatchlistItem> {
  const response = await scanClient.post<WatchlistItem>(
    "/api/v1/watchlist/add",
    { url }
  );

  return response.data;
}

export async function getWatchlistItems(
  filters: WatchlistFilters
): Promise<PageResponse<WatchlistItem>> {

  const response = await scanClient.get<PageResponse<WatchlistItem>>(
    "/api/v1/watchlist/all",
    { params: filters }
  );

  return response.data;
}

export async function removeWatchlistItem(itemId: string): Promise<void> {
  await scanClient.get(`/api/v1/watchlist/remove?itemId=${itemId}`);
}

export async function getWatchlistAlerts(
  filters: WatchlistAlertFilters
): Promise<PageResponse<WatchlistAlert>> {

  const response = await scanClient.get<PageResponse<WatchlistAlert>>(
    "/api/v1/watchlist/alerts",
    { params: filters }
  );

  return response.data;
}

export async function getUnreadAlertCount(): Promise<number> {
  const response = await scanClient.get<number>(
    "/api/v1/watchlist/alerts/unread-count"
  );

  return response.data;
}

export async function markAlertRead(alertId: string): Promise<void> {
  await scanClient.post(`/api/v1/watchlist/alerts/read?alertId=${alertId}`);
}
