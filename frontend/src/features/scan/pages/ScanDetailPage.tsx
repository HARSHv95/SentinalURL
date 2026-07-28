import { useParams } from "react-router-dom";

import PageHeader from "../../../shared/components/PageHeader";

import { Card, CardContent } from "../../../components/ui/card";

import ScanStatusBadge from "../components/ScanStatusBadge";

import { useScan } from "../hooks/useScan";

import { useEffect } from "react";
import { useQueryClient } from "@tanstack/react-query";

import type { ScanSummary } from "../types/scan";

import ScanInfoCard from "../components/ScanInfoCard";
import RiskSummaryCard from "../components/RiskSummaryCard";
import DetectionChart from "../components/DetectionChart";
import OverallVerdict from "../components/OverallVerdict";
import PendingAnalysisCard from "../components/PendingAnalysisCard";
import { CircleAlert } from "lucide-react";

export default function ScanDetailsPage() {
  const { scanId } = useParams();
  const queryClient = useQueryClient();

  const {
    data: scan,
    isLoading,
    error,
  } = useScan(scanId!);

  if (isLoading) {
    return (
      <div className="space-y-6">
        <PageHeader
          title="Scan Details"
          description="Loading scan..."
        />

        <p>Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="space-y-6">
        <PageHeader
          title="Scan Details"
          description="Unable to load scan."
        />

        <p className="text-red-500">
          Failed to load scan.
        </p>
      </div>
    );
  }

  if (!scan) {
    return (
      <div className="space-y-6">
        <PageHeader
          title="Scan Details"
          description="Scan not found."
        />
      </div>
    );
  }

 return (
  <div className="space-y-6">
    <PageHeader
      title="Scan Details"
      description="Detailed URL security report."
    />

    <ScanInfoCard scan={scan} />

    <RiskSummaryCard
      riskReport={scan.riskReport}
      status={scan.status}
    />

    {scan.status === "PENDING" ? (
    <PendingAnalysisCard />
) : (
    <>
        <DetectionChart
            analysisReport={scan.analysisReport}
        />

        <OverallVerdict
            riskReport={scan.riskReport}
        />
    </>
)}
  </div>
);
}