import {
  Shield,
  ShieldAlert,
  ShieldCheck,
  Clock3,
} from "lucide-react";

import LatestReports from "../components/LatestReports";
import QuickActions from "../components/QuickActions";
import RecentActivity from "../components/RecentActivity";
import StatCard from "../../../shared/components/StatCard";
import PageHeader from "../../../shared/components/PageHeader";

import { useReportStats } from "../../report/hooks/useReportStats";

const DashboardPage = () => {
  const { data, isLoading } = useReportStats();

  const summary = data?.summary;

  return (
    <div className="space-y-8">



      {/* Welcome */}
        <PageHeader
        title="Dashboard"
        description="Monitor URL scans and detect malicious websites."
      />

      {/* Stats */}

      <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-4">

        <StatCard
          title="Total Scans"
          value={isLoading ? "—" : summary?.totalScans ?? 0}
          description="No scans yet."
          icon={<Shield size={28} />}
        />

        <StatCard
          title="Safe URLs"
          value={isLoading ? "—" : summary?.safeCount ?? 0}
          description="Everything is safe."
          icon={<ShieldCheck size={28} />}
        />

        <StatCard
          title="Suspicious"
          value={isLoading ? "—" : summary?.suspiciousCount ?? 0}
          description="Nothing suspicious."
          icon={<Clock3 size={28} />}
        />

        <StatCard
          title="Malicious"
          value={isLoading ? "—" : summary?.maliciousCount ?? 0}
          description="No threats found."
          icon={<ShieldAlert size={28} />}
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
