import { useParams } from "react-router-dom";

import PageHeader from "../../../shared/components/PageHeader";

import { useSharedScan } from "../hooks/useSharedScan";
import ScanReportContent from "../components/ScanReportContent";

export default function SharedReportPage() {
  const { shareToken } = useParams();

  const {
    data: scan,
    isLoading,
    error,
  } = useSharedScan(shareToken!);

  if (isLoading) {
    return (
      <div className="mx-auto max-w-4xl space-y-6 p-6">
        <PageHeader
          title="Shared Security Report"
          description="Loading report..."
        />

        <p>Loading...</p>
      </div>
    );
  }

  if (error || !scan) {
    return (
      <div className="mx-auto max-w-4xl space-y-6 p-6">
        <PageHeader
          title="Shared Security Report"
          description="This link is invalid or is no longer being shared."
        />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6 p-6">
      <PageHeader
        title="Shared Security Report"
        description="Read-only view — no login required."
      />

      <ScanReportContent scan={scan} />
    </div>
  );
}
