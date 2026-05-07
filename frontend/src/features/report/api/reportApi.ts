import apiClient from "@/shared/api/apiClient";
import type { GenerateReportPayload } from "../types/Report";

export const generateReport = async (
  payload: GenerateReportPayload,
): Promise<{ blob: Blob; filename: string }> => {
  const response = await apiClient.post("reports/generate", payload, {
    responseType: "blob",
  });

  const disposition = response.headers["content-disposition"] ?? "";
  const match = disposition.match(/filename="(.+)"/);
  const filename = match?.[1] ?? "report.csv";

  return { blob: response.data, filename };
};
