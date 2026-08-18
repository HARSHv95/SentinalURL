import { useParams } from "react-router-dom";
import { Check, FileSearch, Plus } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import { Button } from "../../../components/ui/button";

import { useScan } from "../hooks/useScan";
import { useAddWatchlistItem } from "../../watchlist/hooks/useAddWatchlistItem";

import ScanReportContent from "../components/ScanReportContent";
import ScanReportSkeleton from "../components/ScanReportSkeleton";
import ShareReportDialog from "../components/ShareReportDialog";

export default function ScanDetailsPage() {
  const { scanId } = useParams();

  const {
    data: scan,
    isLoading,
    error,
  } = useScan(scanId!);

  const addWatchlistItem = useAddWatchlistItem();
  const onWatchlist = addWatchlistItem.isSuccess || addWatchlistItem.isError;

  if (isLoading) {
    return (
      <div className="space-y-8">
        <PageHeader
          title="Scan Details"
          description="Loading scan..."
          icon={FileSearch}
        />

        <ScanReportSkeleton />
      </div>
    );
  }

  if (error) {
    return (
      <div className="space-y-8">
        <PageHeader
          title="Scan Details"
          description="Unable to load scan."
          icon={FileSearch}
        />

        <p className="text-destructive">
          Failed to load scan.
        </p>
      </div>
    );
  }

  if (!scan) {
    return (
      <div className="space-y-8">
        <PageHeader
          title="Scan Details"
          description="Scan not found."
          icon={FileSearch}
        />
      </div>
    );
  }

 return (
  <div className="space-y-8">
    <PageHeader
      title="Scan Details"
      description="Detailed URL security report."
      icon={FileSearch}
      actions={
        <>
          <ShareReportDialog
            scanId={scan.scanId}
            shared={scan.shared}
            shareToken={scan.shareToken}
          />

          <Button
            variant="outline"
            disabled={addWatchlistItem.isPending || onWatchlist}
            onClick={() => addWatchlistItem.mutate(scan.url)}
          >
            {onWatchlist ? <Check /> : <Plus />}
            {onWatchlist ? "On Watchlist" : "Add to Watchlist"}
          </Button>
        </>
      }
    />

    <ScanReportContent scan={scan} />
  </div>
);
}
