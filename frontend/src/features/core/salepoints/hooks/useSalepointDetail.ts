import { useQuery } from "@tanstack/react-query";
import { fetchSalepointDetail } from "../api/salepointApi";

export const useSalepointDetail = (mid: string) => {
  return useQuery({
    queryKey: ["salepoint", mid],
    queryFn: () => fetchSalepointDetail(mid),
    enabled: !!mid,
  });
};
