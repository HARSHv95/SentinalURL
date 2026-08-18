import { useParams } from "react-router-dom";
import { Share2, ShieldOff } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import EmptyState from "../../../shared/components/EmptyState";

import { useSharedScan } from "../hooks/useSharedScan";
import ScanReportContent from "../components/ScanReportContent";
import ScanReportSkeleton from "../components/ScanReportSkeleton";

export default function SharedReportPage() {
  const { shareToken } = useParams();

  const {
    data: scan,
    isLoading,
    error,
  } = useSharedScan(shareToken!);

  if (isLoading) {
    return (
      <div className="mx-auto max-w-4xl space-y-8 p-6">
        <PageHeader
          title="Shared Security Report"
          description="Loading report..."
          icon={Share2}
        />

        <ScanReportSkeleton />
      </div>
    );
  }

  if (error || !scan) {
    return (
      <div className="mx-auto max-w-4xl space-y-8 p-6">
        <PageHeader title="Shared Security Report" icon={Share2} />

        <EmptyState
          icon={ShieldOff}
          title="This link isn't available"
          description="It may be invalid, or the owner has stopped sharing this report."
        />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl space-y-8 p-6">
      <PageHeader
        title="Shared Security Report"
        description="Read-only view — no login required."
        icon={Share2}
      />

      <ScanReportContent scan={scan} />
    </div>
  );
}
