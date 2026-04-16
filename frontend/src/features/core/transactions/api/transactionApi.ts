import apiClient from "@/shared/api/apiClient";
import type { TransactionFilters } from "../types/Transaction";

export const fetchTransactions = async (filters: TransactionFilters) => {
  const { data } = await apiClient.get("/transactions", {
    params: filters,
  });
  return data;
};

export const fetchTransactionDetail = async (transactionId: string) => {
  const { data } = await apiClient.get(`/transactions/${transactionId}`);
  return data;
};
