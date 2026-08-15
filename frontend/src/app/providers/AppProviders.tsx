import type { ReactNode } from "react";

import AuthProvider
from "../../features/auth/context/AuthContext";

interface Props {
    children: ReactNode;
}

export default function AppProviders({
    children,
}: Props) {

    return (
        <AuthProvider>
            {children}
        </AuthProvider>
    );
}