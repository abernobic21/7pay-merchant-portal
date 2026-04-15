import { useQuery } from "@tanstack/react-query";
import { fetchMerchants } from "../api/merchantApi";
import type { MerchantFilters } from "../types/Merchant";

export const useMerchants = (filters: MerchantFilters) => {
  return useQuery({
    queryKey: ["merchants", filters],
    queryFn: () => fetchMerchants(filters),
  });
};
