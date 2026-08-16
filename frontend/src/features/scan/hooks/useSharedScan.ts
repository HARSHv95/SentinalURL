import { useQuery } from "@tanstack/react-query";

import { getSharedScan } from "../api/scanApi";

export function useSharedScan(shareToken: string) {
  return useQuery({
    queryKey: ["sharedScan", shareToken],

    queryFn: () => getSharedScan(shareToken),

    enabled: !!shareToken,

    refetchInterval: (query) => {
      const scan = query.state.data;

      if (!scan) return false;

      return scan.status === "PENDING"
        ? 3000
        : false;
    },
  });
}
