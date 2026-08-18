import {
  Shield,
  ShieldAlert,
  ShieldCheck,
  Clock3,
} from "lucide-react";

import LatestReports from "../components/LatestReports";
import QuickActions from "../components/QuickActions";
import RecentActivity from "../components/RecentActivity";
import DashboardHero from "../components/DashboardHero";
import StatCard from "../../../shared/components/StatCard";

import { useReportStats } from "../../report/hooks/useReportStats";

const DashboardPage = () => {
  const { data, isLoading } = useReportStats();

  const summary = data?.summary;

  return (
    <div className="space-y-8">
      <DashboardHero
        totalScans={summary?.totalScans}
        maliciousCount={summary?.maliciousCount}
        isLoading={isLoading}
      />

      <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-4">

        <StatCard
          title="Total Scans"
          value={isLoading ? "—" : summary?.totalScans ?? 0}
          icon={<Shield size={28} />}
          tone="primary"
        />

        <StatCard
          title="Safe URLs"
          value={isLoading ? "—" : summary?.safeCount ?? 0}
          icon={<ShieldCheck size={28} />}
          tone="success"
        />

        <StatCard
          title="Suspicious"
          value={isLoading ? "—" : summary?.suspiciousCount ?? 0}
          icon={<Clock3 size={28} />}
          tone="warning"
        />

        <StatCard
          title="Malicious"
          value={isLoading ? "—" : summary?.maliciousCount ?? 0}
          icon={<ShieldAlert size={28} />}
          tone="destructive"
        />

      </div>

      {/* Lower Section */}

      <div className="grid gap-6 lg:grid-cols-3">

        <div className="lg:col-span-2">
          <RecentActivity />
        </div>

        <QuickActions />

      </div>

      <LatestReports />

    </div>
  );
};

export default DashboardPage;
