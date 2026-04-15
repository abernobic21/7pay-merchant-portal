import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/shared/hooks/useAuth";
import { ROUTES } from "@/router/routes";
import apiClient from "@/shared/api/apiClient";
import type { User } from "@/shared/types/User";
import { login } from "../api/authApi";

export const useLogin = () => {
  const { setUser } = useAuth();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: login,
    onSuccess: async () => {
      try {
        const { data } = await apiClient.get<User>("users/me");
        setUser(data);
        navigate(ROUTES.MERCHANTS);
      } catch {
        navigate(ROUTES.MERCHANTS);
      }
    },
  });
};
