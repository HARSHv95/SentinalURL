import { Clock3 } from "lucide-react";

import DashboardCard from "./DashboardCard";

const RecentActivity = () => {
  return (
    <DashboardCard title="Recent Activity">

      <div className="flex flex-col items-center justify-center py-10 text-center">

        <Clock3
          className="mb-4 text-muted-foreground"
          size={40}
        />

        <p className="font-medium">
          No scans yet
        </p>

        <p className="mt-2 text-sm text-muted-foreground">
          Scan your first URL to see your activity here.
        </p>

      </div>

    </DashboardCard>
  );
};

export default RecentActivity;