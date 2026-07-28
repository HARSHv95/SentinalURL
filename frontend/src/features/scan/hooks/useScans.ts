import { keepPreviousData, useQuery } from "@tanstack/react-query";

import { getScans } from "../api/scanApi";

import type { ScanFilters } from "../types/scan";

export function useScans(filters: ScanFilters) {
  return useQuery({
    queryKey: ["scans", filters],

    queryFn: () => getScans(filters),

    staleTime: 60 * 1000,

    placeholderData: keepPreviousData,

    refetchInterval: (query) => {
      const data = query.state.data;

      if (!data) return false;

      const hasPending = data.content.some(
        (scan) =>
          scan.scanStatus === "PENDING" ||
          scan.scanStatus === "IN_PROGRESS"
      );

      return hasPending ? 3000 : false;
    },
  });
}
