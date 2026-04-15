import apiClient from "@/shared/api/apiClient";
import type { ApiResponse } from "@/shared/types";
import type {
  Merchant,
  MerchantDetail,
  MerchantFilters,
} from "../types/Merchant";

export const fetchMerchants = async (
  filters: MerchantFilters,
): Promise<ApiResponse<Merchant>> => {
  const { data } = await apiClient.get("/merchants", {
    params: {
      page: filters.page,
      size: filters.size,
      sort: filters.sort,
      status: filters.status,
    },
  });

  return data;
};

export const fetchMerchantByOib = async (oib: string) => {
  const { data } = await apiClient.get(`/merchants/${oib}`);
  return data as MerchantDetail;
};
