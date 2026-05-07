export type PeriodType = "DAILY" | "WEEKLY" | "MONTHLY";

export type ReportType = "TRANSACTIONS" | "SUMMARY";

export type ReportFormat = "CSV" | "JSON" | "PDF";

export interface GenerateReportPayload {
  merchantOibs: string[];
  mids: string[];
  periodType: PeriodType;
  timeFrom: string; // ISO 8601, e.g. "2026-01-01T00:00:00"
  timeUntil: string;
  reportType: ReportType;
  format: ReportFormat;
}
