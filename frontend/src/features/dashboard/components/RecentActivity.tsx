import DashboardCard from "./DashboardCard";

import ScanGrid from "../../scan/components/ScanGrid";
import ScanListSkeleton from "../../scan/components/ScanListSkeleton";

import { useScans } from "../../scan/hooks/useScans";

import type { ScanFilters } from "../../scan/types/scan";

const RECENT_ACTIVITY_FILTERS: ScanFilters = {
  page: 0,
  size: 5,
  sort: "newest",
};

const RecentActivity = () => {
  const { data, isLoading } = useScans(RECENT_ACTIVITY_FILTERS);

  return (
    <DashboardCard title="Recent Activity">
      {isLoading ? (
        <ScanListSkeleton count={3} />
      ) : (
        <ScanGrid scans={data?.content ?? []} />
      )}
    </DashboardCard>
  );
};

export default RecentActivity;
