import apiClient from "@/shared/api/apiClient";
import type { SalepointFilters } from "../types/Salepoint";

export const fetchSalepoints = async (filters: SalepointFilters) => {
  const { data } = await apiClient.get("/salepoints", {
    params: filters,
  });
  return data;
};

export const fetchSalepointDetail = async (mid: string) => {
  const { data } = await apiClient.get(`/salepoints/${mid}`);
  return data;
};
