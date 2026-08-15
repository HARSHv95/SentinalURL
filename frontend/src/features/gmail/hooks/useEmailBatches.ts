import { keepPreviousData, useQuery } from "@tanstack/react-query";

import { getEmailBatches } from "../api/gmailApi";

export function useEmailBatches(page: number, size: number) {
  return useQuery({
    queryKey: ["emailBatches", page, size],

    queryFn: () => getEmailBatches(page, size),

    placeholderData: keepPreviousData,
  });
}
