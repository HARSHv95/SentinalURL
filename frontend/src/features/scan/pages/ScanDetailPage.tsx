import { useParams } from "react-router-dom";
import { Check, Plus } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import { Button } from "../../../components/ui/button";

import { useScan } from "../hooks/useScan";
import { useAddWatchlistItem } from "../../watchlist/hooks/useAddWatchlistItem";

import ScanInfoCard from "../components/ScanInfoCard";
import RiskSummaryCard from "../components/RiskSummaryCard";
import DetectionChart from "../components/DetectionChart";
import OverallVerdict from "../components/OverallVerdict";
import PendingAnalysisCard from "../components/PendingAnalysisCard";
import AIThreatAnalysisCard from "../components/AIThreatAnalysisCard";
import RiskBreakdownCard from "../components/RiskBreakdownCard";
import ThreatProviderTable from "../components/ThreatProviderTable";
import DomainIntelligenceCard from "../components/DomainIntelligenceCard";
import DNSRecordsCard from "../components/DNSRecordsCard";
import SSLInformationCard from "../components/SSLInformationCard";

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
      actions={
        <Button
          variant="outline"
          disabled={addWatchlistItem.isPending || onWatchlist}
          onClick={() => addWatchlistItem.mutate(scan.url)}
        >
          {onWatchlist ? <Check /> : <Plus />}
          {onWatchlist ? "On Watchlist" : "Add to Watchlist"}
        </Button>
      }
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

        {scan.aiAnalysis && <AIThreatAnalysisCard analysis={scan.aiAnalysis} />}

        {scan.riskReport && <RiskBreakdownCard factors={scan.riskReport.factors} />}

        {scan.threatIntelligence.length > 0 && (
          <ThreatProviderTable results={scan.threatIntelligence} />
        )}

        {scan.domainIntelligence && (
          <>
            <DomainIntelligenceCard data={scan.domainIntelligence} />

            <div className="grid gap-6 lg:grid-cols-2">
              <DNSRecordsCard records={scan.domainIntelligence.dnsRecords} />
              <SSLInformationCard data={scan.domainIntelligence} />
            </div>
          </>
        )}
    </>
)}
  </div>
);
}
