import { useQuery } from "@tanstack/react-query";
import { fetchMerchants } from "../api/merchantApi";

export const useMerchants = () => {
  return useQuery({
    queryKey: ["merchants"],
    queryFn: fetchMerchants,
  });
};
