import { useMutation } from "@tanstack/react-query";
import { setPassword } from "../api/authApi";

export const useSetPassword = () => {
  return useMutation({
    mutationFn: setPassword,
  });
};
