import { BrowserRouter, Routes, Route } from "react-router-dom";

import DashboardLayout from "../layouts/DashboardLayout";

import DashboardPage from "../../features/dashboard/pages/DashboardPage";
import ScanPage from "../../features/scan/pages/ScanPage";
import ReportPage from "../../features/report/pages/ReportPage";

export default function AppRouter() {
  return (
    <BrowserRouter>

      <Routes>

        <Route element={<DashboardLayout />}>

          <Route
            path="/dashboard"
            element={<DashboardPage />}
          />

          <Route
            path="/scan"
            element={<ScanPage />}
          />

          <Route
            path="/report"
            element={<ReportPage />}
          />

        </Route>

      </Routes>

    </BrowserRouter>
  );
}