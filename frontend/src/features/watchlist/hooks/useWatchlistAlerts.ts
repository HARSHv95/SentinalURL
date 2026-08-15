import { keepPreviousData, useQuery } from "@tanstack/react-query";

import { getWatchlistAlerts } from "../api/watchlistApi";

import type { WatchlistAlertFilters } from "../types/watchlist";

export function useWatchlistAlerts(filters: WatchlistAlertFilters) {
  return useQuery({
    queryKey: ["watchlistAlerts", filters],

    queryFn: () => getWatchlistAlerts(filters),

    placeholderData: keepPreviousData,

    refetchInterval: 45 * 1000,
  });
}
