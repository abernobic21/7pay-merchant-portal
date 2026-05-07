import { useState, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { useQuery } from "@tanstack/react-query";
import { useGenerateReport } from "../hooks/useGenerateReport";
import { useAuth } from "@/shared/hooks/useAuth";
import { getApiError } from "@/shared/utils/getApiError";
import type { GenerateReportPayload, PeriodType } from "../types/Report";
import { ReportTable } from "./ReportTable";
import { fetchAllSalepoints } from "@/features/core/salepoints/api/salepointApi";
import Select from "react-select";

const toISO = (d: Date) => d.toISOString().slice(0, 16);

const defaultTo = () => toISO(new Date());
const defaultFrom = () => {
  const d = new Date();
  d.setDate(d.getDate() - 7);
  return toISO(d);
};

export const ReportsPage = () => {
  const { t } = useTranslation("reports");
  const { user } = useAuth();

  const [selectedOibs, setSelectedOibs] = useState<string[]>(
    user?.merchantId ? [user.merchantId] : [],
  );
  const [selectedMids, setSelectedMids] = useState<string[]>([]);
  const [periodType, setPeriodType] = useState<PeriodType>("MONTHLY");
  const [format, setFormat] = useState<"CSV" | "JSON">("CSV");
  const [timeFrom, setTimeFrom] = useState(defaultFrom());
  const [timeUntil, setTimeUntil] = useState(defaultTo());
  const [rangeError, setRangeError] = useState<string | null>(null);

  const { data: salepoints = [], isLoading: salepointsLoading } = useQuery({
    queryKey: ["salepoints-all"],
    queryFn: fetchAllSalepoints,
    staleTime: 5 * 60 * 1000,
  });

  const merchantOibs = useMemo(
    () => [...new Set(salepoints.map((s) => s.merchantOib))].sort(),
    [salepoints],
  );

  const availableMids = useMemo(
    () => [...new Set(salepoints.map((s) => s.mid))].sort(),
    [salepoints],
  );

  const validateAndSetRange = (from: string, to: string) => {
    if (new Date(to) <= new Date(from)) {
      setRangeError(t("filters.rangeError.toBeforeFrom"));
    } else {
      setRangeError(null);
    }
  };

  const handleTimeFrom = (value: string) => {
    setTimeFrom(value);
    validateAndSetRange(value, timeUntil);
  };

  const handleTimeUntil = (value: string) => {
    setTimeUntil(value);
    validateAndSetRange(timeFrom, value);
  };

  const {
    mutate: generateReport,
    isPending,
    isError,
    error,
    reportData,
    reportFilename,
    reportBlob,
  } = useGenerateReport();

  const canGenerate =
    selectedOibs.length > 0 &&
    selectedMids.length > 0 &&
    !rangeError &&
    !isPending;

  const handleGenerate = () => {
    if (!canGenerate) return;
    const payload: GenerateReportPayload = {
      merchantOibs: selectedOibs,
      mids: selectedMids,
      periodType,
      timeFrom: new Date(timeFrom).toISOString(),
      timeUntil: new Date(timeUntil).toISOString(),
      reportType: "TRANSACTIONS",
      format: format,
    };
    generateReport(payload);
  };

  return (
    <div>
      <h1>{t("title")}</h1>

      <div>
        <label>{t("filters.merchantOib")}</label>
        <Select
          isMulti
          isLoading={salepointsLoading}
          options={[
            { value: "__all__", label: t("filters.selectAllOibs") },
            ...merchantOibs.map((oib) => ({ value: oib, label: oib })),
          ]}
          value={selectedOibs.map((oib) => ({ value: oib, label: oib }))}
          onChange={(selected) => {
            if (selected.some((s) => s.value === "__all__")) {
              setSelectedOibs([...merchantOibs]);
            } else {
              setSelectedOibs(selected.map((s) => s.value));
            }
          }}
          placeholder={t("filters.selectMerchantOib")}
        />

        <label>{t("filters.mid")}</label>
        <Select
          isMulti
          isLoading={salepointsLoading}
          options={[
            { value: "__all__", label: t("filters.selectAllMids") },
            ...availableMids.map((mid) => ({ value: mid, label: mid })),
          ]}
          value={selectedMids.map((mid) => ({ value: mid, label: mid }))}
          onChange={(selected) => {
            if (selected.some((s) => s.value === "__all__")) {
              setSelectedMids([...availableMids]);
            } else {
              setSelectedMids(selected.map((s) => s.value));
            }
          }}
          placeholder={t("filters.selectMid")}
        />

        <select
          value={periodType}
          onChange={(e) => setPeriodType(e.target.value as PeriodType)}
        >
          <option value="DAILY">{t("periodTypes.daily")}</option>
          <option value="WEEKLY">{t("periodTypes.weekly")}</option>
          <option value="MONTHLY">{t("periodTypes.monthly")}</option>
        </select>

        <select
          value={format}
          onChange={(e) => setFormat(e.target.value as "CSV" | "JSON")}
        >
          <option value="CSV">CSV</option>
          <option value="JSON">JSON</option>
        </select>

        <input
          type="datetime-local"
          value={timeFrom}
          onChange={(e) => handleTimeFrom(e.target.value)}
        />

        <input
          type="datetime-local"
          value={timeUntil}
          onChange={(e) => handleTimeUntil(e.target.value)}
        />

        {rangeError && <p>{rangeError}</p>}

        <button onClick={handleGenerate} disabled={!canGenerate}>
          {isPending ? t("actions.generating") : t("actions.generate")}
        </button>

        {reportData && reportBlob && (
          <ReportTable
            data={reportData}
            filename={reportFilename}
            blob={reportBlob}
          />
        )}
      </div>

      {isError && <p>{getApiError(error)}</p>}
    </div>
  );
};
