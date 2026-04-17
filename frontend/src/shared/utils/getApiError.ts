import axios from "axios";
import i18n from "../i18n";

export const getApiError = (error: unknown, context?: string): string => {
  if (!axios.isAxiosError(error)) return i18n.t("errors:unknown");

  const status = error.response?.status;

  const keys = [
    context ? `errors:${context}.${status}` : null,
    `errors:${status}`,
    "errors:unknown",
  ].filter(Boolean) as string[];

  for (const key of keys) {
    if (i18n.exists(key)) return i18n.t(key);
  }

  return i18n.t("errors:unknown");
};
