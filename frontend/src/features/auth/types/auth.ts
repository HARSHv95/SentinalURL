export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}

export interface User {
    firstName: string;
    lastName: string;
    email: string;
    role: "USER" | "ADMIN";
}

export interface AuthResponse {
    accessToken: string;
    tokenType: string;
    expiresIn: number;
    user: User;
}

export interface AuthContextType {
    token: string | null;
    user: User | null;
    isAuthenticated: boolean;

    login: (data: AuthResponse) => void;
    logout: () => void;
}