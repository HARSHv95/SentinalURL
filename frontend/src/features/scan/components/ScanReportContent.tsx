import ScanInfoCard from "./ScanInfoCard";
import RiskSummaryCard from "./RiskSummaryCard";
import DetectionChart from "./DetectionChart";
import OverallVerdict from "./OverallVerdict";
import PendingAnalysisCard from "./PendingAnalysisCard";
import AIThreatAnalysisCard from "./AIThreatAnalysisCard";
import RiskBreakdownCard from "./RiskBreakdownCard";
import ThreatProviderTable from "./ThreatProviderTable";
import DomainIntelligenceCard from "./DomainIntelligenceCard";
import DNSRecordsCard from "./DNSRecordsCard";
import SSLInformationCard from "./SSLInformationCard";

import type { CreateScanResponse } from "../types/scan";

interface Props {
  scan: CreateScanResponse;
}

export default function ScanReportContent({ scan }: Props) {
  return (
    <>
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
    </>
  );
}
