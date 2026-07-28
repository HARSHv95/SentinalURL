import { FileText, History, ScanSearch } from "lucide-react";

import { Button } from "../../../components/ui/button";
import DashboardCard from "./DashboardCard";

import { useNavigate } from "react-router-dom";
import { ROUTES } from "../../../shared/lib/routes";

const QuickActions = () => {
    const navigate = useNavigate();
  return (
    <DashboardCard title="Quick Actions">

      <div className="grid gap-3">

        <Button
  className="justify-start gap-2"
  onClick={() => navigate(ROUTES.SCAN)}
>
  <ScanSearch size={18} />
  Scan URL
</Button>

<Button
  variant="outline"
  className="justify-start gap-2"
  onClick={() => navigate(ROUTES.HISTORY)}
>
  <History size={18} />
  View History
</Button>

<Button
  variant="outline"
  className="justify-start gap-2"
  onClick={() => navigate(ROUTES.REPORTS)}
>
  <FileText size={18} />
  Generate Report
</Button>

      </div>

    </DashboardCard>
  );
};

export default QuickActions;