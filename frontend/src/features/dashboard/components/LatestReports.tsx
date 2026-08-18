import DashboardCard from "./DashboardCard";

import TopRiskyDomainsList from "../../report/components/TopRiskyDomainsList";
import { Skeleton } from "../../../components/ui/skeleton";

import { useReportStats } from "../../report/hooks/useReportStats";

const LatestReports = () => {
  const { data, isLoading } = useReportStats(30);

  return (
    <DashboardCard title="Latest Reports">
      {isLoading ? (
        <div className="space-y-3">
          <Skeleton className="h-10 rounded-lg" />
          <Skeleton className="h-10 rounded-lg" />
          <Skeleton className="h-10 rounded-lg" />
        </div>
      ) : (
        <TopRiskyDomainsList data={data?.topRiskyDomains ?? []} />
      )}
    </DashboardCard>
  );
};

export default LatestReports;
