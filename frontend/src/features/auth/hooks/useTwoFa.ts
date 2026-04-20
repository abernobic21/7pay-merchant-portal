import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "@/shared/hooks/useAuth";
import { ROUTES } from "@/router/routes";
import apiClient from "@/shared/api/apiClient";
import { twoFaValidate, twoFaValidateBackup } from "../api/authApi";
import type { UserProfile } from "@/features/users/types/User";

export const useTwoFa = () => {
  const { setUser } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [useBackup, setUseBackup] = useState(false);

  const preAuthToken = location.state?.preAuthToken as string | undefined;

  const onSuccess = async () => {
    try {
      const { data } = await apiClient.get<UserProfile>("/users/me");
      setUser(data);
      navigate(ROUTES.DASHBOARD);
    } catch {
      navigate(ROUTES.DASHBOARD);
    }
  };

  const totpMutation = useMutation({
    mutationFn: (code: string) => {
      if (!preAuthToken) throw new Error("Missing preAuthToken");
      return twoFaValidate({ preAuthToken, code });
    },
    onSuccess,
  });

  const backupMutation = useMutation({
    mutationFn: (backupCode: string) => {
      if (!preAuthToken) throw new Error("Missing preAuthToken");
      return twoFaValidateBackup({ preAuthToken, backupCode });
    },
    onSuccess,
  });

  return {
    useBackup,
    setUseBackup,
    preAuthToken,
    totpMutation,
    backupMutation,
  };
};
