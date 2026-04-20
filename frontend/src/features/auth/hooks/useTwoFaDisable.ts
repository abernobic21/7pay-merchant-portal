import { useMutation, useQueryClient } from "@tanstack/react-query";
import { twoFaDisable } from "../api/authApi";

export const useTwoFaDisable = (onSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (password: string) => twoFaDisable({ password }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["me"] });
      onSuccess();
    },
  });
};
