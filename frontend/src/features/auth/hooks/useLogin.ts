import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/shared/hooks/useAuth";
import { ROUTES } from "@/router/routes";
import apiClient from "@/shared/api/apiClient";
import { login } from "../api/authApi";
import type { UserProfile } from "@/features/users/types/User";

export const useLogin = () => {
  const { setUser } = useAuth();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: login,
    onSuccess: async (data) => {
      if (data.requires2fa && data.preAuthToken) {
        navigate(ROUTES.TWO_FA, { state: { preAuthToken: data.preAuthToken } });
        return;
      }

      try {
        const { data: profile } = await apiClient.get<UserProfile>("/users/me");
        setUser(profile);
        navigate(ROUTES.DASHBOARD);
      } catch {
        navigate(ROUTES.DASHBOARD);
      }
    },
  });
};
