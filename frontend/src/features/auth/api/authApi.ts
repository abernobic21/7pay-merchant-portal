import apiClient from "@/shared/api/apiClient";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  expiresIn: number;
  firstLogin: boolean;
}

export interface SetPasswordRequest {
  token: string;
  newPassword: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

export const login = (payload: LoginRequest) =>
  apiClient
    .post<LoginResponse>("/auth/login", payload)
    .then(({ data }) => data);

export const logout = () => apiClient.post("auth/logout");

export const setPassword = (payload: SetPasswordRequest) =>
  apiClient.post("auth/set-password", payload);

export const forgotPassword = (payload: ForgotPasswordRequest) =>
  apiClient.post("auth/forgot-password", payload);

export const resetPassword = (payload: ResetPasswordRequest) =>
  apiClient.post("auth/reset-password", payload);
