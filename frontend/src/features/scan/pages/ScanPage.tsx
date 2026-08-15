import { useState } from "react";

import PageHeader from "../../../shared/components/PageHeader";
import { cn } from "../../../lib/utils";

import UrlScanForm from "../components/UrlScanForm";
import QrUploadInput from "../components/QrUploadInput";
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

type InputMode = "url" | "qr";

export default function ScanPage() {

  const {
    data,
    isLoading,
  } = useScans(RECENT_SCANS_FILTERS);

  const scans = data?.content ?? [];

  const createScan = useCreateScan();
  const [mode, setMode] = useState<InputMode>("url");
  const [decodedUrl, setDecodedUrl] = useState<string | undefined>(undefined);

  const handleSubmit = (data: ScanFormData) => {
    createScan.mutate(data);
  };

  return (
    <div className="space-y-8">
      <PageHeader
        title="URL Scanner"
        description="Analyze a URL for potential threats."
      />

      <div className="flex gap-2">
        <button
          type="button"
          className={cn(
            "rounded-lg px-3 py-1.5 text-sm font-medium",
            mode === "url" ? "bg-primary text-primary-foreground" : "bg-muted text-muted-foreground"
          )}
          onClick={() => setMode("url")}
        >
          URL
        </button>
        <button
          type="button"
          className={cn(
            "rounded-lg px-3 py-1.5 text-sm font-medium",
            mode === "qr" ? "bg-primary text-primary-foreground" : "bg-muted text-muted-foreground"
          )}
          onClick={() => setMode("qr")}
        >
          QR code
        </button>
      </div>

      {mode === "qr" && (
        <QrUploadInput onDecoded={(url) => setDecodedUrl(url)} />
      )}

      <UrlScanForm
        onSubmit={handleSubmit}
        isLoading={createScan.isPending}
        defaultUrl={decodedUrl}
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
