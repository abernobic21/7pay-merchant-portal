import apiClient from "@/shared/api/apiClient";
import type { Salepoint } from "../types/Salepoint";
import type { ApiResponse } from "@/shared/types";

export const fetchSalepoints = async (): Promise<ApiResponse<Salepoint>> => {
  const { data } = await apiClient.get("salepoints");
  return data;
};
