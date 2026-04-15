import { useQuery } from "@tanstack/react-query";
import { fetchMerchantByOib } from "../api/merchantApi";

export const useMerchantDetail = (oib?: string) => {
  return useQuery({
    queryKey: ["merchant", oib],
    queryFn: () => fetchMerchantByOib(oib!),
    enabled: !!oib,
  });
};
