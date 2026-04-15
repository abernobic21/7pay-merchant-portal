import apiClient from "@/shared/api/apiClient";
import type { DashboardStats, DashboardFilters } from "../types/Dashboard";

const formatTime = (isoString: string): string => {
  return new Date(isoString).toISOString().slice(0, 19);
};

export const getDashboardStats = async (
  filters: DashboardFilters,
): Promise<DashboardStats> => {
  const { data } = await apiClient.get("/dashboard/stats", {
    params: {
      ...filters,
      timeFrom: formatTime(filters.timeFrom),
      timeUntil: formatTime(filters.timeUntil),
    },
  });
  return data;
};
