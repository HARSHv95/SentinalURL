import {
  LayoutDashboard,
  ScanSearch,
  History,
  FileText,
} from "lucide-react";

import { ROUTES } from "./routes";

export const navigation = [
  {
    title: "Dashboard",
    path: ROUTES.DASHBOARD,
    icon: LayoutDashboard,
  },
  {
    title: "Scan URL",
    path: ROUTES.SCAN,
    icon: ScanSearch,
  },
  {
    title: "History",
    path: ROUTES.HISTORY,
    icon: History,
  },
  {
    title: "Reports",
    path: ROUTES.REPORTS,
    icon: FileText,
  },
];
