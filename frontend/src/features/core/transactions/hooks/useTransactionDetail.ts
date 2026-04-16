import { useQuery } from "@tanstack/react-query";
import { fetchTransactionDetail } from "../api/transactionApi";

export const useTransactionDetail = (transactionId: string) => {
  return useQuery({
    queryKey: ["transaction-detail", transactionId],
    queryFn: () => fetchTransactionDetail(transactionId),
    enabled: !!transactionId,
  });
};
