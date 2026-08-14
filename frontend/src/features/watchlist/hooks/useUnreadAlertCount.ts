import { useQuery } from "@tanstack/react-query";

import { getUnreadAlertCount } from "../api/watchlistApi";

export function useUnreadAlertCount() {
  return useQuery({
    queryKey: ["watchlistAlerts", "unreadCount"],

    queryFn: () => getUnreadAlertCount(),

    refetchInterval: 45 * 1000,
  });
}
