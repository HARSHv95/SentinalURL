import { useQuery } from "@tanstack/react-query";

import { getReportStats } from "../api/reportApi";

export function useReportStats(days = 30) {
  return useQuery({
    queryKey: ["report-stats", days],

    queryFn: () => getReportStats(days),

    staleTime: 30 * 1000,

    refetchInterval: 30 * 1000,
  });
}
