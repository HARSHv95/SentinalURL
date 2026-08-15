import { keepPreviousData, useQuery } from "@tanstack/react-query";

import { getWatchlistItems } from "../api/watchlistApi";

import type { WatchlistFilters } from "../types/watchlist";

export function useWatchlistItems(filters: WatchlistFilters) {
  return useQuery({
    queryKey: ["watchlistItems", filters],

    queryFn: () => getWatchlistItems(filters),

    staleTime: 60 * 1000,

    placeholderData: keepPreviousData,
  });
}
