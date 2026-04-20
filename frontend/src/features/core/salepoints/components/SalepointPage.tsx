import { getApiError } from "@/shared/utils/getApiError";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useSalepoints } from "../hooks/useSalepoints";
import type { SalepointFilters } from "../types/Salepoint";
import { SalepointDetailModal } from "./SalepointDetailModal";
import { SalepointTable } from "./SalepointTable";

export const SalepointsPage = () => {
  const { t } = useTranslation("salepoints");

  const [filters, setFilters] = useState<SalepointFilters>({
    page: 0,
    size: 10,
  });

  const [selectedMid, setSelectedMid] = useState<string | null>(null);

  const { data, isLoading, isError, error } = useSalepoints(filters);

  const [merchantOib, setMerchantOib] = useState("");

  const handleSearch = () => {
    setFilters((p) => ({
      ...p,
      page: 0,
      merchantOib: merchantOib || undefined,
    }));
  };

  const handlePageChange = (page: number) => {
    setFilters((p) => ({ ...p, page }));
  };
  const handleSortChange = (sort: string) => {
    setFilters((prev) => ({ ...prev, page: 0, sort }));
  };

  if (isError) return <p>{getApiError(error, "salepoints")}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <div>
        <input
          placeholder={t("filters.merchantOib")}
          value={merchantOib}
          onChange={(e) => setMerchantOib(e.target.value)}
        />
        <button onClick={handleSearch} disabled={!merchantOib}>
          {t("actions.search")}
        </button>
      </div>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <SalepointTable
          salepoints={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          currentSort={filters.sort}
          onPageChange={handlePageChange}
          onSelect={setSelectedMid}
          onSort={handleSortChange}
        />
      )}

      {selectedMid && (
        <SalepointDetailModal
          key={selectedMid}
          mid={selectedMid}
          onClose={() => setSelectedMid(null)}
        />
      )}
    </div>
  );
};
