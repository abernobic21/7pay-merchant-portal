import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTerminals } from "../hooks/useTerminals";
import { TerminalTable } from "./TerminalTable";
import { TerminalDetailModal } from "./TerminalDetailModal";
import type { TerminalFilters } from "../types/Terminal";
import { getApiError } from "@/shared/utils/getApiError";

export const TerminalsPage = () => {
  const { t } = useTranslation("terminals");

  const [filters, setFilters] = useState<TerminalFilters>({
    page: 0,
    size: 10,
  });

  const [selectedTid, setSelectedTid] = useState<string | null>(null);

  const { data, isLoading, isError, error } = useTerminals(filters);

  const handlePageChange = (page: number) => {
    setFilters((p) => ({ ...p, page }));
  };

  const handleChange = (field: keyof TerminalFilters, value: string) => {
    setFilters((p) => ({
      ...p,
      page: 0,
      [field]: value || undefined,
    }));
  };

  if (isError) return <p>{getApiError(error)}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <div>
        <input
          placeholder={t("filters.merchantOib")}
          value={filters.merchantOib ?? ""}
          onChange={(e) => handleChange("merchantOib", e.target.value)}
        />

        <input
          placeholder={t("filters.mid")}
          value={filters.mid ?? ""}
          onChange={(e) => handleChange("mid", e.target.value)}
        />
      </div>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <TerminalTable
          terminals={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          onPageChange={handlePageChange}
          onSelect={setSelectedTid}
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
