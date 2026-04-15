import apiClient from "@/shared/api/apiClient";
import type { ApiResponse } from "@/shared/types";
import type {
  User,
  CreateUserPayload,
  UpdateUserPayload,
  UpdateStatusPayload,
  UserFilters,
  UserProfile,
  ChangePasswordPayload,
} from "../types/User";

export const getUsers = async (
  filters: UserFilters,
): Promise<ApiResponse<User>> => {
  const { data } = await apiClient.get("/users", { params: filters });
  return data;
};

export const getUserById = async (id: string): Promise<User> => {
  const { data } = await apiClient.get(`/users/${id}`);
  return data;
};

export const createUser = async (payload: CreateUserPayload): Promise<User> => {
  const { data } = await apiClient.post("/users", payload);
  return data;
};

export const updateUser = async (
  id: string,
  payload: UpdateUserPayload,
): Promise<User> => {
  const { data } = await apiClient.put(`/users/${id}`, payload);
  return data;
};

export const deleteUser = async (id: string): Promise<void> => {
  await apiClient.delete(`/users/${id}`);
};

export const updateStatus = async (
  id: string,
  payload: UpdateStatusPayload,
): Promise<void> => {
  await apiClient.patch(`/users/${id}/status`, payload);
};

export const getMe = async (): Promise<UserProfile> => {
  const { data } = await apiClient.get("/users/me");
  return data;
};

export const changePassword = async (
  payload: ChangePasswordPayload,
): Promise<void> => {
  await apiClient.put("/users/me/password", payload);
};
