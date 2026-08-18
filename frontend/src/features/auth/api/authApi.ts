import { authClient } from "../../../shared/api/apiClient";

import type {
  LoginRequest,
  RegisterRequest,
  AuthResponse,
} from "../types/auth";

export const login = async (
  data: LoginRequest
): Promise<AuthResponse> => {
  const response = await authClient.post("/api/v1/auth/login", {
    emailId: data.email,
    password: data.password
  });
  
  return response.data;
};

export const register = async (
  data: RegisterRequest
): Promise<void> => {
  await authClient.post("/api/v1/auth/register", {
    firstName: data.firstName,
    lastName: data.lastName,
    emailId: data.email,
    password: data.password,
  });
};