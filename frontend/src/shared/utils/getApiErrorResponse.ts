import type { AxiosError } from "axios";
import type { ApiErrorResponse } from "../types/api";

export const getApiErrorMessage = (error: unknown): string => {
  const axiosError = error as AxiosError<ApiErrorResponse>;

  return (
    axiosError.response?.data?.message ??
    axiosError.message ??
    "Unexpected error"
  );
};
