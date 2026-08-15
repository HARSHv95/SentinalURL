import { AlertTriangle, Globe2, Lock, ShieldAlert } from "lucide-react";

import { DropdownMenuItem } from "../../../components/ui/dropdown-menu";

import { useMarkAlertRead } from "../hooks/useMarkAlertRead";

import type { WatchlistAlert, WatchlistAlertType } from "../types/watchlist";

interface Props {
  alert: WatchlistAlert;
}

const TYPE_ICON: Record<WatchlistAlertType, typeof Lock> = {
  SSL_EXPIRING: Lock,
  SSL_CHANGED: Lock,
  VERDICT_CHANGED: ShieldAlert,
  DNS_CHANGED: Globe2,
  WHOIS_CHANGED: AlertTriangle,
};

export default function AlertListItem({ alert }: Props) {
  const markRead = useMarkAlertRead();
  const Icon = TYPE_ICON[alert.type];

  return (
    <DropdownMenuItem
      className="flex items-start gap-2 whitespace-normal py-2"
      onClick={() => {
        if (!alert.read) markRead.mutate(alert.id);
      }}
    >
      <Icon className="mt-0.5 size-4 shrink-0 text-muted-foreground" />

      <div className="min-w-0 flex-1">
        <p className="truncate text-sm font-medium">{alert.url}</p>
        <p className="text-xs text-muted-foreground">{alert.message}</p>
        <p className="mt-0.5 text-xs text-muted-foreground">
          {new Date(alert.createdAt).toLocaleString()}
        </p>
      </div>

      {!alert.read && (
        <span className="mt-1 size-2 shrink-0 rounded-full bg-primary" />
      )}
    </DropdownMenuItem>
  );
}
