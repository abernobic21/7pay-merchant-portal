export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: "admin" | "user";
  accessLevel: "merchant" | "branch";
  merchantId: string | null;
  status: "active" | "deactivated";
  firstLoginFlag: boolean;
  createdAt: string;
}

export interface UserProfile {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: "admin" | "user";
  accessLevel: "merchant" | "branch";
  merchantId: string | null;
  status: "active" | "deactivated";
  twoFaEnabled: boolean;
}

export interface CreateUserPayload {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: "admin" | "user";
  accessLevel: "merchant" | "branch";
  merchantId?: string;
}

export interface UpdateUserPayload {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  role?: "admin" | "user";
  accessLevel?: "merchant" | "branch";
  merchantId?: string;
}

export interface UpdateStatusPayload {
  status: "active" | "deactivated";
}

export interface UserFilters {
  page?: number;
  size?: number;
  sort?: string;
  status?: "active" | "deactivated";
  role?: "admin" | "user";
}

export interface ChangePasswordPayload {
  currentPassword: string;
  newPassword: string;
}
