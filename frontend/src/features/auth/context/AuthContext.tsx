import {
    createContext,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from "react";

import type { AuthContextType, AuthResponse, User } from "../types/auth";
import {
    getToken,
    getUser,
    removeToken,
    removeUser,
    saveToken,
    saveUser,
} from "../../../shared/utils/token";

export const AuthContext =
    createContext<AuthContextType | null>(null);

interface Props {
    children: ReactNode;
}



export default function AuthProvider({
    children,
}: Props) {

    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {

    setToken(getToken());

    setUser(getUser());

}, []);

    useEffect(() => {

        const storedToken = getToken();

        if (storedToken) {
            setToken(storedToken);
        }

    }, []);

    const login = (response: AuthResponse) => {

    saveToken(response.accessToken);

    saveUser(response.user);

    setToken(response.accessToken);

    setUser(response.user);
};

    const logout = () => {

    removeToken();
    removeUser();

    setToken(null);
    setUser(null);
};

    const value = useMemo(() => ({
    token,
    user,
    isAuthenticated: !!token,
    login,
    logout,
}), [token, user]);

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}