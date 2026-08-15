import { useQuery } from "@tanstack/react-query";

import { getGmailStatus } from "../api/gmailApi";

export function useGmailStatus() {
  return useQuery({
    queryKey: ["gmailStatus"],

    queryFn: () => getGmailStatus(),
  });
}
