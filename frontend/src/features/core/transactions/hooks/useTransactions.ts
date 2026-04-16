import { useQuery } from "@tanstack/react-query";
import { fetchTransactions } from "../api/transactionApi";
import type { TransactionFilters } from "../types/Transaction";

export const useTransactions = (
  filters: TransactionFilters,
  enabled: boolean,
) => {
  return useQuery({
    queryKey: ["transactions", filters],
    queryFn: () => fetchTransactions(filters),
    enabled,
  });
};
