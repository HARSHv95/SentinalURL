import { useSearchParams } from "react-router-dom";
import { Eye } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import PaginationBar from "../../../shared/components/PaginationBar";
import EmptyState from "../../../shared/components/EmptyState";
import { Button } from "../../../components/ui/button";

import WatchlistGrid from "../components/WatchlistGrid";
import WatchlistToolbar from "../components/WatchlistToolbar";
import WatchlistListSkeleton from "../components/WatchlistListSkeleton";
import AddToWatchlistForm from "../components/AddToWatchlistForm";

import { useWatchlistItems } from "../hooks/useWatchlistItems";
import { useAddWatchlistItem } from "../hooks/useAddWatchlistItem";
import { parseWatchlistFilters } from "../lib/filters";

import type { WatchlistFilters } from "../types/watchlist";

export default function WatchlistPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const filters = parseWatchlistFilters(searchParams);
  const { data, isLoading, isFetching } = useWatchlistItems(filters);
  const addItem = useAddWatchlistItem();

  const updateFilters = (next: WatchlistFilters) => {
    const params: Record<string, string> = {
      page: String(next.page),
      size: String(next.size),
      sort: next.sort,
    };
    if (next.search) params.search = next.search;
    if (next.verdict) params.verdict = next.verdict;
    setSearchParams(params);
  };

  const hasActiveFilters = Boolean(filters.search || filters.verdict);

  return (
    <div className="space-y-8">
      <PageHeader
        title="Watchlist"
        description="Keep an eye on URLs — get alerted when their SSL, reputation, or DNS/WHOIS records change."
        icon={Eye}
      />

      <AddToWatchlistForm
        isLoading={addItem.isPending}
        onSubmit={(data) => addItem.mutate(data.url)}
      />

      <WatchlistToolbar
        filters={filters}
        onFiltersChange={(next) => updateFilters({ ...next, page: 0 })}
      />

      {isLoading ? (
        <WatchlistListSkeleton count={filters.size} />
      ) : (
        <WatchlistGrid
          items={data?.content ?? []}
          emptyState={
            <EmptyState
              title={
                hasActiveFilters
                  ? "No watchlist items match your filters"
                  : "Your watchlist is empty"
              }
              description={
                hasActiveFilters
                  ? "Try adjusting your search or filters."
                  : "Add a URL above to start monitoring it."
              }
              action={
                hasActiveFilters ? (
                  <Button
                    variant="outline"
                    onClick={() => setSearchParams({})}
                  >
                    Clear filters
                  </Button>
                ) : undefined
              }
            />
          }
        />
      )}

      {data && data.totalPages > 1 && (
        <PaginationBar
          page={data.number}
          totalPages={data.totalPages}
          onPageChange={(page) => updateFilters({ ...filters, page })}
          isFetching={isFetching}
        />
      )}
    </div>
  );
}
