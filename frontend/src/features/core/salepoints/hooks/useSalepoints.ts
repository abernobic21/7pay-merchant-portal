import { useQuery } from "@tanstack/react-query";
import type { SalepointFilters } from "../types/Salepoint";
import { fetchSalepoints } from "../api/salepointApi";

export const useSalepoints = (filters: SalepointFilters) => {
  return useQuery({
    queryKey: ["salepoints", filters],
    queryFn: () => fetchSalepoints(filters),
  });
};
