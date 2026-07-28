import { BrowserRouter, Routes, Route } from "react-router-dom";

import DashboardLayout from "../layouts/DashboardLayout";


import DashboardPage from "../../features/dashboard/pages/DashboardPage";
import ScanPage from "../../features/scan/pages/ScanPage";
import ReportPage from "../../features/report/pages/ReportPage";
import { ROUTES } from "../../shared/lib/routes";
import AuthLayout from "../layouts/AuthLayouts";
import LoginPage from "../../features/auth/pages/LoginPage";
import RegisterPage from "../../features/auth/pages/RegisterPage";
import ProtectedRoute from "../../shared/components/ProtectedRoute";
import PublicRoute from "../../shared/components/PublicRoute";
import ScanDetailsPage from "../../features/scan/pages/ScanDetailPage";
import ScanHistoryPage from "../../features/scan/pages/ScanHistoryPage";

export default function AppRouter() {
  return (
    <BrowserRouter>

      <Routes>
        {/* Public routes */}
     <Route element={<PublicRoute />}>
            <Route element={<AuthLayout />}>

                <Route
                    path={ROUTES.LOGIN}
                    element={<LoginPage />}
                />

                <Route
                    path={ROUTES.REGISTER}
                    element={<RegisterPage />}
                />

            </Route>
        </Route>

        <Route element={<ProtectedRoute />}>

    <Route element={<DashboardLayout />}>

        <Route
            path={ROUTES.DASHBOARD}
            element={<DashboardPage />}
        />

        <Route
            path={ROUTES.SCAN}
            element={<ScanPage />}
        />
        <Route
            path={ROUTES.HISTORY}
            element={<ScanHistoryPage />}
        />
        <Route
            path={ROUTES.REPORTS}
            element={<ReportPage />}
        />
        <Route
            path={ROUTES.SCAN_DETAIL}
            element={<ScanDetailsPage />}
        />
    </Route>

</Route>

      </Routes>

    </BrowserRouter>
  );
}