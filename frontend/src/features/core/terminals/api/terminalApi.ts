import apiClient from "@/shared/api/apiClient";
import type { TerminalFilters } from "../types/Terminal";

export const fetchTerminals = async (filters: TerminalFilters) => {
  const { data } = await apiClient.get("/terminals", {
    params: filters,
  });
  return data;
};

export const fetchTerminalDetail = async (tid: string) => {
  const { data } = await apiClient.get(`/terminals/${tid}`);
  return data;
};
