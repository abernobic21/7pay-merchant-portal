import { useQuery } from "@tanstack/react-query";
import { fetchTerminals } from "../api/terminalApi";
import type { TerminalFilters } from "../types/Terminal";

export const useTerminals = (filters: TerminalFilters) => {
  return useQuery({
    queryKey: ["terminals", filters],
    queryFn: () => fetchTerminals(filters),
  });
};
