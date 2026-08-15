import { useMutation, useQueryClient } from "@tanstack/react-query";

import { disconnectGmail } from "../api/gmailApi";

export function useDisconnectGmail() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => disconnectGmail(),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["gmailStatus"] });
    },
  });
}
