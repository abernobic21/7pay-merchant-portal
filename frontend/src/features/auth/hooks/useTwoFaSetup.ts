import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { twoFaSetup, twoFaVerifySetup } from "../api/authApi";

export const useTwoFaSetup = (onSuccess: () => void) => {
  const queryClient = useQueryClient();
  const [step, setStep] = useState<"qr" | "verify" | "codes">("qr");

  const setupQuery = useQuery({
    queryKey: ["2fa-setup"],
    queryFn: twoFaSetup,
    staleTime: Infinity,
    refetchOnWindowFocus: false,
  });

  const verifyMutation = useMutation({
    mutationFn: (code: string) => twoFaVerifySetup({ code }),
    onSuccess: () => {
      setStep("codes");
      queryClient.invalidateQueries({ queryKey: ["me"] });
    },
  });

  const handleVerify = (code: string) => verifyMutation.mutate(code);

  const handleDone = () => {
    queryClient.invalidateQueries({ queryKey: ["2fa-setup"] });
    onSuccess();
  };

  return {
    step,
    setupQuery,
    verifyMutation,
    handleVerify,
    handleDone,
  };
};
