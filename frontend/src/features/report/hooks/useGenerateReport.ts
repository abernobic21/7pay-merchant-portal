import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { generateReport } from "../api/reportApi";

export const useGenerateReport = () => {
  const [reportData, setReportData] = useState<string[][] | null>(null);
  const [reportFilename, setReportFilename] = useState<string>("report.csv");
  const [reportBlob, setReportBlob] = useState<Blob | null>(null);

  const mutation = useMutation({
    mutationFn: generateReport,
    onSuccess: async ({ blob, filename }) => {
      const text = await blob.text();
      let rows: string[][];

      if (filename.endsWith(".json")) {
        const json: Record<string, string>[] = JSON.parse(text);
        if (json.length === 0) {
          setReportData([]);
          return;
        }
        const headers = Object.keys(json[0]);
        const dataRows = json.map((row) => headers.map((h) => row[h] ?? ""));
        rows = [headers, ...dataRows];
      } else {
        rows = text
          .trim()
          .split("\n")
          .map((row) => row.split(","));
      }

      setReportData(rows);
      setReportFilename(filename);
      setReportBlob(blob);
    },
  });

  return { ...mutation, reportData, reportFilename, reportBlob };
};
