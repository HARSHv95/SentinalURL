import { Globe, Trash2 } from "lucide-react";

import { Card, CardContent } from "../../../components/ui/card";
import { Badge } from "../../../components/ui/badge";
import { Button } from "../../../components/ui/button";

import { VERDICT_META } from "../../scan/lib/verdict";

import { useRemoveWatchlistItem } from "../hooks/useRemoveWatchlistItem";

import type { WatchlistItem } from "../types/watchlist";

interface Props {
  item: WatchlistItem;
}

export default function WatchlistCard({ item }: Props) {

  const removeItem = useRemoveWatchlistItem();

  return (
    <Card>
      <CardContent className="space-y-3 p-5">

        <div className="flex items-start justify-between gap-3">

          <div className="flex min-w-0 items-center gap-2">
            <Globe className="size-4 shrink-0 text-muted-foreground" />
            <h3 className="truncate font-semibold">
              {item.url}
            </h3>
          </div>

          <div className="flex shrink-0 items-center gap-2">
            {item.lastVerdict && (
              <Badge variant={VERDICT_META[item.lastVerdict].badgeVariant}>
                {VERDICT_META[item.lastVerdict].label}
              </Badge>
            )}

            <Button
              variant="ghost"
              size="icon-sm"
              aria-label="Remove from watchlist"
              disabled={removeItem.isPending}
              onClick={() => removeItem.mutate(item.id)}
            >
              <Trash2 className="text-muted-foreground" />
            </Button>
          </div>

        </div>

        <div className="flex justify-between text-sm text-muted-foreground">

          <span>
            {item.lastCheckedAt
              ? `Last checked ${new Date(item.lastCheckedAt).toLocaleString()}`
              : "Not yet checked"}
          </span>

          <span>
            Added {new Date(item.createdAt).toLocaleDateString()}
          </span>

        </div>

      </CardContent>
    </Card>
  );
}
