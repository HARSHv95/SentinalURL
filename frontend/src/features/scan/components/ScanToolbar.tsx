import { useEffect, useState } from "react";
import { Search } from "lucide-react";

import { Input } from "../../../components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../../components/ui/select";

import { useDebouncedValue } from "../../../shared/hooks/useDebouncedValue";

import { VERDICT_META } from "../lib/verdict";

import type {
  RiskVerdict,
  ScanFilters,
  ScanSortOption,
  ScanStatus,
} from "../types/scan";

interface ScanToolbarProps {
  filters: ScanFilters;
  onFiltersChange: (next: ScanFilters) => void;
}

const STATUS_OPTIONS: { value: ScanStatus | "ALL"; label: string }[] = [
  { value: "ALL", label: "All Statuses" },
  { value: "PENDING", label: "Pending" },
  { value: "IN_PROGRESS", label: "In Progress" },
  { value: "COMPLETED", label: "Completed" },
  { value: "FAILED", label: "Failed" },
];

const VERDICT_OPTIONS: { value: RiskVerdict | "ALL"; label: string }[] = [
  { value: "ALL", label: "All Verdicts" },
  { value: "SAFE", label: VERDICT_META.SAFE.label },
  { value: "LOW_RISK", label: VERDICT_META.LOW_RISK.label },
  { value: "MEDIUM_RISK", label: VERDICT_META.MEDIUM_RISK.label },
  { value: "HIGH_RISK", label: VERDICT_META.HIGH_RISK.label },
  { value: "CRITICAL", label: VERDICT_META.CRITICAL.label },
];

const SORT_OPTIONS: { value: ScanSortOption; label: string }[] = [
  { value: "newest", label: "Newest First" },
  { value: "oldest", label: "Oldest First" },
  { value: "risk_high", label: "Highest Risk" },
  { value: "risk_low", label: "Lowest Risk" },
];

export default function ScanToolbar({
  filters,
  onFiltersChange,
}: ScanToolbarProps) {
  const [searchInput, setSearchInput] = useState(filters.search ?? "");
  const debouncedSearch = useDebouncedValue(searchInput, 400);

  useEffect(() => {
    setSearchInput(filters.search ?? "");
  }, [filters.search]);

  useEffect(() => {
    const trimmed = debouncedSearch.trim();
    if (trimmed === (filters.search ?? "")) return;
    onFiltersChange({ ...filters, search: trimmed || undefined });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedSearch]);

  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
      <div className="relative flex-1">
        <Search className="pointer-events-none absolute top-1/2 left-2.5 size-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search by URL..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          className="pl-8"
        />
      </div>

      <Select
        value={filters.status ?? "ALL"}
        onValueChange={(value) =>
          onFiltersChange({
            ...filters,
            status: value === "ALL" ? undefined : (value as ScanStatus),
          })
        }
      >
        <SelectTrigger className="sm:w-44">
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          {STATUS_OPTIONS.map((option) => (
            <SelectItem key={option.value} value={option.value}>
              {option.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select
        value={filters.verdict ?? "ALL"}
        onValueChange={(value) =>
          onFiltersChange({
            ...filters,
            verdict: value === "ALL" ? undefined : (value as RiskVerdict),
          })
        }
      >
        <SelectTrigger className="sm:w-44">
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          {VERDICT_OPTIONS.map((option) => (
            <SelectItem key={option.value} value={option.value}>
              {option.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select
        value={filters.sort}
        onValueChange={(value) =>
          onFiltersChange({ ...filters, sort: value as ScanSortOption })
        }
      >
        <SelectTrigger className="sm:w-44">
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          {SORT_OPTIONS.map((option) => (
            <SelectItem key={option.value} value={option.value}>
              {option.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
}
