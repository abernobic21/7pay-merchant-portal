import { useMutation } from "@tanstack/react-query";
import { changePassword } from "../api/usersApi";

export const useChangePassword = () => {
  return useMutation({
    mutationFn: changePassword,
  });
};
