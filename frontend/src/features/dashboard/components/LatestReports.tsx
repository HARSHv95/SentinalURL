import { FileSearch } from "lucide-react";

import DashboardCard from "./DashboardCard";

const LatestReports = () => {
  return (
    <DashboardCard title="Latest Reports">

      <div className="flex flex-col items-center py-10 text-center">

        <FileSearch
          className="mb-4 text-muted-foreground"
          size={40}
        />

        <p>No reports generated.</p>

        <p className="mt-2 text-sm text-muted-foreground">
          Reports will appear here once created.
        </p>

      </div>

    </DashboardCard>
  );
};

export default LatestReports;