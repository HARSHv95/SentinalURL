import { useMutation, useQueryClient } from "@tanstack/react-query";

import { shareScan } from "../api/scanApi";

export function useShareScan(scanId: string) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => shareScan(scanId),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["scan", scanId] });
    },
  });
}
