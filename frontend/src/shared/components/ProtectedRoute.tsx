import { Navigate, Outlet } from "react-router-dom";

import useAuth from "../../features/auth/hooks/useAuth";
import { ROUTES } from "../lib/routes";

export default function ProtectedRoute() {

    const { isAuthenticated } = useAuth();

    if (!isAuthenticated) {

        return (
            <Navigate
                to={ROUTES.LOGIN}
                replace
            />
        );
    }

    return <Outlet />;
}