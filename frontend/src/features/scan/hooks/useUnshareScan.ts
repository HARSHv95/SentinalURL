import { useMutation, useQueryClient } from "@tanstack/react-query";

import { unshareScan } from "../api/scanApi";

export function useUnshareScan(scanId: string) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => unshareScan(scanId),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["scan", scanId] });
    },
  });
}
