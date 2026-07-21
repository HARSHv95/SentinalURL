import {
  LayoutDashboard,
  ScanSearch,
  History,
  FileText,
  User,
} from "lucide-react";

export const navigation = [
  {
    title: "Dashboard",
    path: "/dashboard",
    icon: LayoutDashboard,
  },
  {
    title: "Scan URL",
    path: "/scan",
    icon: ScanSearch,
  },
  {
    title: "History",
    path: "/history",
    icon: History,
  },
  {
    title: "Reports",
    path: "/report",
    icon: FileText,
  },
  {
    title: "Profile",
    path: "/profile",
    icon: User,
  },
];