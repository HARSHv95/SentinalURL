import { useMutation, useQueryClient } from "@tanstack/react-query";

import { syncGmailNow } from "../api/gmailApi";

export function useSyncGmailNow() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => syncGmailNow(),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["gmailStatus"] });
      queryClient.invalidateQueries({ queryKey: ["emailBatches"] });
      queryClient.invalidateQueries({ queryKey: ["scans"] });
    },
  });
}
