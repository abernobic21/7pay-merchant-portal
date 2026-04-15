import apiClient from "@/shared/api/apiClient";
import type { ApiResponse } from "@/shared/types";
import type { Merchant } from "../types/Merchant";

export const fetchMerchants = async (): Promise<ApiResponse<Merchant>> => {
  const { data } = await apiClient.get("/merchants");
  return data;
};
