import { ChevronLeft, ChevronRight } from "lucide-react";

import { Button } from "../../components/ui/button";
import { cn } from "../../lib/utils";

interface PaginationBarProps {
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  isFetching?: boolean;
}

const WINDOW_SIZE = 2;

function getPageWindow(page: number, totalPages: number): (number | "ellipsis")[] {
  const pages: (number | "ellipsis")[] = [];

  const start = Math.max(0, page - WINDOW_SIZE);
  const end = Math.min(totalPages - 1, page + WINDOW_SIZE);

  if (start > 0) {
    pages.push(0);
    if (start > 1) pages.push("ellipsis");
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  if (end < totalPages - 1) {
    if (end < totalPages - 2) pages.push("ellipsis");
    pages.push(totalPages - 1);
  }

  return pages;
}

const PaginationBar = ({
  page,
  totalPages,
  onPageChange,
  isFetching,
}: PaginationBarProps) => {
  const pages = getPageWindow(page, totalPages);

  return (
    <nav
      aria-label="Pagination"
      className="flex items-center justify-center gap-1"
    >
      <Button
        variant="outline"
        size="icon-sm"
        disabled={page === 0 || isFetching}
        onClick={() => onPageChange(page - 1)}
        aria-label="Previous page"
      >
        <ChevronLeft />
      </Button>

      {pages.map((entry, index) =>
        entry === "ellipsis" ? (
          <span
            key={`ellipsis-${index}`}
            className="px-2 text-sm text-muted-foreground"
          >
            …
          </span>
        ) : (
          <Button
            key={entry}
            variant="outline"
            size="icon-sm"
            disabled={isFetching}
            onClick={() => onPageChange(entry)}
            className={cn(
              entry === page &&
                "border-primary bg-primary text-primary-foreground hover:bg-primary/80"
            )}
            aria-current={entry === page ? "page" : undefined}
          >
            {entry + 1}
          </Button>
        )
      )}

      <Button
        variant="outline"
        size="icon-sm"
        disabled={page >= totalPages - 1 || isFetching}
        onClick={() => onPageChange(page + 1)}
        aria-label="Next page"
      >
        <ChevronRight />
      </Button>
    </nav>
  );
};

export default PaginationBar;
