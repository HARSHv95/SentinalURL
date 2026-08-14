import { Bell } from "lucide-react";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../../../components/ui/dropdown-menu";

import AlertListItem from "./AlertListItem";

import { useUnreadAlertCount } from "../hooks/useUnreadAlertCount";
import { useWatchlistAlerts } from "../hooks/useWatchlistAlerts";

export default function AlertsBell() {
  const { data: unreadCount } = useUnreadAlertCount();
  const { data } = useWatchlistAlerts({ page: 0, size: 10 });

  const alerts = data?.content ?? [];
  const hasUnread = Boolean(unreadCount && unreadCount > 0);

  return (
    <DropdownMenu>
      <DropdownMenuTrigger className="relative hover:text-primary transition">
        <Bell size={20} />
        {hasUnread && (
          <span className="absolute -top-1 -right-1 flex size-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-semibold text-white">
            {unreadCount! > 9 ? "9+" : unreadCount}
          </span>
        )}
      </DropdownMenuTrigger>

      <DropdownMenuContent align="end" className="w-80">
        <DropdownMenuGroup>
          <DropdownMenuLabel>Watchlist Alerts</DropdownMenuLabel>

          <DropdownMenuSeparator />

          {alerts.length === 0 ? (
            <p className="px-1.5 py-3 text-center text-sm text-muted-foreground">
              No alerts yet.
            </p>
          ) : (
            alerts.map((alert) => (
              <AlertListItem key={alert.id} alert={alert} />
            ))
          )}
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
