import type { ReactNode } from "react";

import ScanCard from "./ScanCard";
import EmptyState from "../../../shared/components/EmptyState";

import type { ScanSummary } from "../types/scan";

interface Props {
  scans: ScanSummary[];
  emptyState?: ReactNode;
}

export default function ScanGrid({
  scans,
  emptyState,
}: Props) {

  if (scans.length === 0) {
    return (
      <>{emptyState ?? <EmptyState title="No scans yet." />}</>
    );
  }

  return (

    <div className="grid gap-4">

      {scans.map(scan => (

        <ScanCard
          key={scan.scanId}
          scan={scan}
        />

      ))}

    </div>

  );

}
