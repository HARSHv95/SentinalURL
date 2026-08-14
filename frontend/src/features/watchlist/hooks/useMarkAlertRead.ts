import { useMutation, useQueryClient } from "@tanstack/react-query";

import { markAlertRead } from "../api/watchlistApi";

export function useMarkAlertRead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (alertId: string) => markAlertRead(alertId),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["watchlistAlerts"] });
    },
  });
}
