import type { ReactNode } from "react";

import WatchlistCard from "./WatchlistCard";
import EmptyState from "../../../shared/components/EmptyState";

import type { WatchlistItem } from "../types/watchlist";

interface Props {
  items: WatchlistItem[];
  emptyState?: ReactNode;
}

export default function WatchlistGrid({
  items,
  emptyState,
}: Props) {

  if (items.length === 0) {
    return (
      <>{emptyState ?? <EmptyState title="Your watchlist is empty." />}</>
    );
  }

  return (
    <div className="grid gap-4">
      {items.map(item => (
        <WatchlistCard
          key={item.id}
          item={item}
        />
      ))}
    </div>
  );
}
