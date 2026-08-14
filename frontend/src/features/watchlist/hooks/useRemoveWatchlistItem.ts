import { useMutation, useQueryClient } from "@tanstack/react-query";

import { removeWatchlistItem } from "../api/watchlistApi";

export function useRemoveWatchlistItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (itemId: string) => removeWatchlistItem(itemId),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["watchlistItems"] });
    },
  });
}
