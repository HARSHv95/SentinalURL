import PageHeader from "../../../shared/components/PageHeader";

import UrlScanForm from "../components/UrlScanForm";
import ScanGrid from "../components/ScanGrid";

import { useCreateScan } from "../hooks/useCreateScan";
import { useScans } from "../hooks/useScans";

import type { ScanFilters } from "../types/scan";
import type { ScanFormData } from "../schemas/scanSchema";

const RECENT_SCANS_FILTERS: ScanFilters = {
  page: 0,
  size: 5,
  sort: "newest",
};

export default function ScanPage() {

  const {
    data,
    isLoading,
  } = useScans(RECENT_SCANS_FILTERS);

  const scans = data?.content ?? [];

  const createScan = useCreateScan();

  const handleSubmit = (data: ScanFormData) => {
    createScan.mutate(data);
  };

  return (
    <div className="space-y-8">
      <PageHeader
        title="URL Scanner"
        description="Analyze a URL for potential threats."
      />

      <UrlScanForm
        onSubmit={handleSubmit}
        isLoading={createScan.isPending}
      />

      <section className="space-y-4">
        <h2 className="text-xl font-semibold">
          Recent Scans
        </h2>

        {isLoading ? (
          <p>Loading scans...</p>
        ) : (
          <ScanGrid scans={scans} />
        )}
      </section>
    </div>
  );
}
