import { useNavigate, useSearchParams } from "react-router-dom";
import { Plus } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import PaginationBar from "../../../shared/components/PaginationBar";
import EmptyState from "../../../shared/components/EmptyState";
import { Button } from "../../../components/ui/button";

import ScanGrid from "../components/ScanGrid";
import ScanToolbar from "../components/ScanToolbar";
import ScanListSkeleton from "../components/ScanListSkeleton";

import { useScans } from "../hooks/useScans";
import { parseScanFilters } from "../lib/filters";
import { ROUTES } from "../../../shared/lib/routes";

import type { ScanFilters } from "../types/scan";

export default function ScanHistoryPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const filters = parseScanFilters(searchParams);
  const { data, isLoading, isFetching } = useScans(filters);

  const updateFilters = (next: ScanFilters) => {
    const params: Record<string, string> = {
      page: String(next.page),
      size: String(next.size),
      sort: next.sort,
    };
    if (next.search) params.search = next.search;
    if (next.status) params.status = next.status;
    if (next.verdict) params.verdict = next.verdict;
    setSearchParams(params);
  };

  const hasActiveFilters = Boolean(
    filters.search || filters.status || filters.verdict
  );

  return (
    <div className="space-y-8">
      <PageHeader
        title="Scan History"
        description="Browse, search, and filter all your past scans."
        actions={
          <Button onClick={() => navigate(ROUTES.SCAN)}>
            <Plus />
            New Scan
          </Button>
        }
      />

      <ScanToolbar
        filters={filters}
        onFiltersChange={(next) => updateFilters({ ...next, page: 0 })}
      />

      {isLoading ? (
        <ScanListSkeleton count={filters.size} />
      ) : (
        <ScanGrid
          scans={data?.content ?? []}
          emptyState={
            <EmptyState
              title={
                hasActiveFilters
                  ? "No scans match your filters"
                  : "No scans yet"
              }
              description={
                hasActiveFilters
                  ? "Try adjusting your search or filters."
                  : "Run your first URL scan to see it here."
              }
              action={
                hasActiveFilters ? (
                  <Button
                    variant="outline"
                    onClick={() => setSearchParams({})}
                  >
                    Clear filters
                  </Button>
                ) : (
                  <Button onClick={() => navigate(ROUTES.SCAN)}>
                    New Scan
                  </Button>
                )
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
