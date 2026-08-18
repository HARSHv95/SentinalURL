import { Shield, ShieldAlert, ShieldCheck, Clock3, FileBarChart, FileText } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";
import StatCard from "../../../shared/components/StatCard";
import EmptyState from "../../../shared/components/EmptyState";
import { Skeleton } from "../../../components/ui/skeleton";
import { Button } from "../../../components/ui/button";

import ScanGrid from "../../scan/components/ScanGrid";

import { useReportStats } from "../hooks/useReportStats";
import VerdictDistributionChart from "../components/VerdictDistributionChart";
import RiskDistributionChart from "../components/RiskDistributionChart";
import DailyScanCountChart from "../components/DailyScanCountChart";
import TopRiskyDomainsList from "../components/TopRiskyDomainsList";

const ReportPage = () => {
  const { data, isLoading } = useReportStats(30);

  return (
    <div className="space-y-8">
      <PageHeader
        title="Reports"
        description="Analytics and insights across all your scans."
        icon={FileText}
        actions={
          <Button disabled title="Coming soon">
            Export Report
          </Button>
        }
      />

      {isLoading ? (
        <div className="space-y-8">
          <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-4">
            {Array.from({ length: 4 }).map((_, i) => (
              <Skeleton key={i} className="h-32 rounded-xl" />
            ))}
          </div>
          <div className="grid gap-6 lg:grid-cols-2">
            <Skeleton className="h-80 rounded-xl" />
            <Skeleton className="h-80 rounded-xl" />
          </div>
        </div>
      ) : !data || data.summary.totalScans === 0 ? (
        <EmptyState
          icon={FileBarChart}
          title="No data yet"
          description="Run your first scan to start seeing analytics here."
        />
      ) : (
        <>
          <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-4">
            <StatCard
              title="Total Scans"
              value={data.summary.totalScans}
              icon={<Shield size={28} />}
              tone="primary"
            />
            <StatCard
              title="Safe URLs"
              value={data.summary.safeCount}
              icon={<ShieldCheck size={28} />}
              tone="success"
            />
            <StatCard
              title="Malicious URLs"
              value={data.summary.maliciousCount}
              icon={<ShieldAlert size={28} />}
              tone="destructive"
            />
            <StatCard
              title="Pending"
              value={data.summary.pendingCount}
              icon={<Clock3 size={28} />}
              tone="warning"
            />
          </div>

          <div className="grid gap-6 lg:grid-cols-2">
            <VerdictDistributionChart data={data.verdictDistribution} />
            <RiskDistributionChart data={data.riskDistribution} />
          </div>

          <DailyScanCountChart data={data.dailyScanCounts} />

          <div className="grid gap-6 lg:grid-cols-2">
            <TopRiskyDomainsList data={data.topRiskyDomains} />

            <div className="space-y-4">
              <h2 className="text-lg font-semibold">Recent Threats</h2>
              <ScanGrid
                scans={data.recentThreats}
                emptyState={
                  <EmptyState
                    icon={ShieldCheck}
                    title="No recent threats"
                    description="Nothing risky has been detected recently."
                  />
                }
              />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default ReportPage;
