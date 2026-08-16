import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

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
import WatchlistPage from "../../features/watchlist/pages/WatchlistPage";
import IntegrationsPage from "../../features/gmail/pages/IntegrationsPage";
import SharedReportPage from "../../features/scan/pages/SharedReportPage";

export default function AppRouter() {
  return (
    <BrowserRouter>

      <Routes>
        {/* Unguarded — no auth required, no redirect for logged-in users either */}
        <Route
            path={ROUTES.SHARED_REPORT}
            element={<SharedReportPage />}
        />

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

    <Route
        path="/"
        element={<Navigate to={ROUTES.DASHBOARD} replace />}
    />

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
        <Route
            path={ROUTES.WATCHLIST}
            element={<WatchlistPage />}
        />
        <Route
            path={ROUTES.SETTINGS_INTEGRATIONS}
            element={<IntegrationsPage />}
        />
    </Route>

    <Route
        path="*"
        element={<Navigate to={ROUTES.DASHBOARD} replace />}
    />

</Route>

      </Routes>

    </BrowserRouter>
  );
}