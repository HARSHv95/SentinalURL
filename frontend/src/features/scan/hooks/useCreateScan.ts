import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";

import { createScan } from "../api/scanApi";

export function useCreateScan() {

  const queryClient = useQueryClient();

  return useMutation({

    mutationFn: createScan,

    onSuccess() {
      queryClient.invalidateQueries({ queryKey: ["scans"] });
    },

  });

}
