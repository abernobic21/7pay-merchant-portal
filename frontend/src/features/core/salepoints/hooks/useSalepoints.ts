import { useQuery } from "@tanstack/react-query";
import { fetchSalepoints } from "../api/salepointApi";

export const useSalepoints = () => {
  return useQuery({
    queryKey: ["salepoints"],
    queryFn: fetchSalepoints,
  });
};
