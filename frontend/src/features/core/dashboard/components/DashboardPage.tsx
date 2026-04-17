import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useDashboardStats } from "../hooks/useDashboardStats";
import type { DashboardFilters } from "../types/Dashboard";
import { getApiError } from "@/shared/utils/getApiError";

const getDefaultFilters = (): DashboardFilters => {
  const now = new Date();
  const sevenDaysAgo = new Date();
  sevenDaysAgo.setDate(now.getDate() - 7);

  return {
    timeFrom: sevenDaysAgo.toISOString().slice(0, 16),
    timeUntil: now.toISOString().slice(0, 16),
  };
};

export const DashboardPage = () => {
  const { t } = useTranslation("dashboard");

  const [appliedFilters, setAppliedFilters] =
    useState<DashboardFilters>(getDefaultFilters);
  const [draftFilters, setDraftFilters] =
    useState<DashboardFilters>(getDefaultFilters);

  const { data, isLoading, isError, error } = useDashboardStats(appliedFilters);

  const [rangeError, setRangeError] = useState<string | null>(null);

  const handleChange = (field: keyof DashboardFilters, value: string) => {
    setDraftFilters((prev) => {
      const updated = { ...prev, [field]: value || undefined };

      const from = field === "timeFrom" ? value : prev.timeFrom;
      const to = field === "timeUntil" ? value : prev.timeUntil;

      if (from && to) {
        setRangeError(
          new Date(to) <= new Date(from)
            ? t("filters.rangeError.toBeforeFrom")
            : null,
        );
      }

      return updated;
    });
  };

  const handleApply = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (rangeError) return;
    setAppliedFilters(draftFilters);
  };

  return (
    <div>
      <div>
        <h1>{t("title")}</h1>
        {data?.cachedAt && (
          <p>
            {t("lastUpdated")}: {new Date(data.cachedAt).toLocaleTimeString()}
          </p>
        )}
      </div>
      {/* filters */}
      <form onSubmit={handleApply}>
        <input
          type="datetime-local"
          value={draftFilters.timeFrom}
          onChange={(e) => handleChange("timeFrom", e.target.value)}
          required
        />
        <input
          type="datetime-local"
          value={draftFilters.timeUntil}
          onChange={(e) => handleChange("timeUntil", e.target.value)}
          required
        />

        <input
          type="text"
          placeholder={t("filters.mid")}
          value={draftFilters.mid ?? ""}
          onChange={(e) => handleChange("mid", e.target.value)}
        />
        <input
          type="text"
          placeholder={t("filters.tid")}
          value={draftFilters.tid ?? ""}
          onChange={(e) => handleChange("tid", e.target.value)}
        />
        {rangeError && <p>{rangeError}</p>}

        <button type="submit" disabled={!!rangeError}>
          {t("filters.apply")}
        </button>
      </form>
      {isLoading && <p>{t("loading")}</p>}{" "}
      {isError && <p>{getApiError(error)}</p>}
      {data && (
        <>
          {/* stat cards */}
          <div>
            <div>
              <p>{t("stats.totalRevenue")}</p>
              <p>{data.totalRevenue}</p>
            </div>
            <div>
              <p>{t("stats.totalTransactions")}</p>
              <p>{data.totalTransactions}</p>
            </div>
            <div>
              <p>{t("stats.avgTransactionValue")}</p>
              <p>{data.avgTransactionValue}</p>
            </div>
          </div>

          {/* revenue by card brand */}
          <div>
            <h2>{t("charts.revenueByCardBrand")}</h2>
            <table>
              <thead>
                <tr>
                  <th>{t("charts.cardBrand")}</th>
                  <th>{t("charts.revenue")}</th>
                  <th>{t("charts.count")}</th>
                </tr>
              </thead>
              <tbody>
                {data.revenueByCardBrand.map((item) => (
                  <tr key={item.cardBrand}>
                    <td>{item.cardBrand}</td>
                    <td>{item.revenue}</td>
                    <td>{item.count}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* revenue by acquirer */}
          <div>
            <h2>{t("charts.revenueByAcquirer")}</h2>
            <table>
              <thead>
                <tr>
                  <th>{t("charts.acquirerHost")}</th>
                  <th>{t("charts.revenue")}</th>
                  <th>{t("charts.count")}</th>
                </tr>
              </thead>
              <tbody>
                {data.revenueByAcquirer.map((item) => (
                  <tr key={item.acquirerHost}>
                    <td>{item.acquirerHost}</td>
                    <td>{item.revenue}</td>
                    <td>{item.count}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
};
