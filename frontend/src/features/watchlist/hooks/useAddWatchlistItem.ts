import { useMutation, useQueryClient } from "@tanstack/react-query";

import { addWatchlistItem } from "../api/watchlistApi";

export function useAddWatchlistItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (url: string) => addWatchlistItem(url),

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["watchlistItems"] });
    },
  });
}
