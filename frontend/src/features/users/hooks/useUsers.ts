import { useQuery } from "@tanstack/react-query";
import { getUsers } from "../api/usersApi";
import type { UserFilters } from "../types/User";

export const useUsers = (filters: UserFilters) => {
  return useQuery({
    queryKey: ["users", filters],
    queryFn: () => getUsers(filters),
  });
};
