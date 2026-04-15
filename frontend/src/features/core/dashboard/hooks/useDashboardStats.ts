import { useQuery } from "@tanstack/react-query";
import { getDashboardStats } from "../api/dashboardApi";
import type { DashboardFilters } from "../types/Dashboard";

export const useDashboardStats = (filters: DashboardFilters) => {
  return useQuery({
    queryKey: ["dashboard", filters],
    queryFn: () => getDashboardStats(filters),
  });
};
