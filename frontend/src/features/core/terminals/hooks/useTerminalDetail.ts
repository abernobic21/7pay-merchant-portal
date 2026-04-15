import { useQuery } from "@tanstack/react-query";
import { fetchTerminalDetail } from "../api/terminalApi";

export const useTerminalDetail = (tid: string) => {
  return useQuery({
    queryKey: ["terminal-detail", tid],
    queryFn: () => fetchTerminalDetail(tid),
    enabled: !!tid,
  });
};
