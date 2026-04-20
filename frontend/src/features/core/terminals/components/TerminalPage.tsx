import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTerminals } from "../hooks/useTerminals";
import { TerminalTable } from "./TerminalTable";
import { TerminalDetailModal } from "./TerminalDetailModal";
import type { TerminalFilters } from "../types/Terminal";
import { getApiError } from "@/shared/utils/getApiError";
import { PageSizeSelect } from "@/shared/components/PageSizeSelect";

export const TerminalsPage = () => {
  const { t } = useTranslation("terminals");

  const [filters, setFilters] = useState<TerminalFilters>({
    page: 0,
    size: 10,
  });

  const [selectedTid, setSelectedTid] = useState<string | null>(null);

  const { data, isLoading, isError, error } = useTerminals(filters);

  const [merchantOib, setMerchantOib] = useState("");
  const [mid, setMid] = useState("");

  const handleSearch = () => {
    setFilters((p) => ({
      ...p,
      page: 0,
      merchantOib: merchantOib || undefined,
      mid: mid || undefined,
    }));
  };

  const handleSortChange = (sort: string) => {
    setFilters((p) => ({ ...p, page: 0, sort }));
  };

  const handlePageChange = (page: number) => {
    setFilters((p) => ({ ...p, page }));
  };

  const handleSizeChange = (size: number) => {
    setFilters((prev) => ({ ...prev, page: 0, size }));
  };

  if (isError) return <p>{getApiError(error)}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <div>
        <input
          placeholder={t("filters.merchantOib")}
          value={merchantOib}
          onChange={(e) => setMerchantOib(e.target.value)}
        />
        <input
          placeholder={t("filters.mid")}
          value={mid}
          onChange={(e) => setMid(e.target.value)}
        />

        <PageSizeSelect value={filters.size} onChange={handleSizeChange} />

        <button onClick={handleSearch}>{t("actions.search")}</button>
      </div>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <TerminalTable
          terminals={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          currentSort={filters.sort}
          onPageChange={handlePageChange}
          onSelect={setSelectedTid}
          onSort={handleSortChange}
        />
      )}

      {selectedTid && (
        <TerminalDetailModal
          key={selectedTid}
          tid={selectedTid}
          onClose={() => setSelectedTid(null)}
        />
      )}
    </div>
  );
};
