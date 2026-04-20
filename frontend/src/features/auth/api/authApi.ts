import apiClient from "@/shared/api/apiClient";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  expiresIn: number;
  firstLogin: boolean;
  requires2fa?: boolean;
  preAuthToken?: string;
}
export interface TwoFaValidateRequest {
  preAuthToken: string;
  code: string;
}

export interface TwoFaValidateBackupRequest {
  preAuthToken: string;
  backupCode: string;
}

export interface TwoFaSetupResponse {
  secret: string;
  qrCodeUrl: string;
  backupCodes: string[];
}

export interface TwoFaVerifySetupRequest {
  code: string;
}

export interface TwoFaDisableRequest {
  password: string;
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

export const twoFaValidate = (payload: TwoFaValidateRequest) =>
  apiClient
    .post<LoginResponse>("/auth/2fa/validate", payload)
    .then(({ data }) => data);

export const twoFaValidateBackup = (payload: TwoFaValidateBackupRequest) =>
  apiClient
    .post<LoginResponse>("/auth/2fa/validate-backup", payload)
    .then(({ data }) => data);

export const twoFaSetup = () =>
  apiClient
    .post<TwoFaSetupResponse>("/auth/2fa/setup")
    .then(({ data }) => data);

export const twoFaVerifySetup = (payload: TwoFaVerifySetupRequest) =>
  apiClient.post("/auth/2fa/verify-setup", payload);

export const twoFaDisable = (payload: TwoFaDisableRequest) =>
  apiClient.post("/auth/2fa/disable", payload);
