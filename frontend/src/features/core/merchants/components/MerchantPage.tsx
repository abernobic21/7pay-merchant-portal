import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useMerchants } from "../hooks/useMerchants";
import type { MerchantFilters } from "../types/Merchant";
import { MerchantDetailModal } from "./MerchantDetailModal";
import { MerchantTable } from "./MerchantTable";

export const MerchantsPage = () => {
  const { t } = useTranslation("merchants");

  const [filters, setFilters] = useState<MerchantFilters>({
    page: 0,
    size: 10,
  });

  const [selectedOib, setSelectedOib] = useState<string | null>(null);

  const { data, isLoading, isError } = useMerchants(filters);

  const handlePageChange = (page: number) => {
    setFilters((p) => ({ ...p, page }));
  };

  const handleStatusChange = (status?: string) => {
    setFilters((p) => ({ ...p, page: 0, status }));
  };

  if (isError) return <p>{t("error")}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <select
        value={filters.status ?? ""}
        onChange={(e) => handleStatusChange(e.target.value || undefined)}
      >
        <option value="">{t("filters.all")}</option>
        <option value="ACTIVE">{t("status.active")}</option>
        <option value="BLOCKED">{t("status.blocked")}</option>
      </select>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <MerchantTable
          merchants={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          onPageChange={handlePageChange}
          onSelect={(oib) => setSelectedOib(oib)}
        />
      )}

      {/* ✅ THIS WAS MISSING */}
      {selectedOib && (
        <MerchantDetailModal
          key={selectedOib}
          oib={selectedOib}
          onClose={() => setSelectedOib(null)}
        />
      )}
    </div>
  );
};
