import { useQuery } from "@tanstack/react-query";

import { getScan } from "../api/scanApi";

export function useScan(scanId: string) {
  return useQuery({
    queryKey: ["scan", scanId],

    queryFn: () => getScan(scanId),

    enabled: !!scanId,

    refetchInterval: (query) => {
  const scan = query.state.data;

  if (!scan) return false;

  return scan.status === "PENDING"
    ? 3000
    : false;
},
  });
}