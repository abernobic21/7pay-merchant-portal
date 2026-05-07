import apiClient from "@/shared/api/apiClient";
import type { Salepoint, SalepointFilters } from "../types/Salepoint";

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

export const fetchAllSalepoints = async (): Promise<Salepoint[]> => {
  const first = await fetchSalepoints({ page: 0, size: 100 });
  const totalPages = first.page.totalPages;

  if (totalPages <= 1) return first.content;

  const rest = await Promise.all(
    Array.from({ length: totalPages - 1 }, (_, i) =>
      fetchSalepoints({ page: i + 1, size: 100 }),
    ),
  );

  return [first.content, ...rest.map((r) => r.content)].flat();
};
