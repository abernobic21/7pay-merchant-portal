import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useMerchants } from "../hooks/useMerchants";
import type { MerchantFilters, MerchantStatus } from "../types/Merchant";
import { MerchantDetailModal } from "./MerchantDetailModal";
import { MerchantTable } from "./MerchantTable";
import { getApiError } from "@/shared/utils/getApiError";

export const MerchantsPage = () => {
  const { t } = useTranslation("merchants");

  const [filters, setFilters] = useState<MerchantFilters>({
    page: 0,
    size: 10,
  });

  const [selectedOib, setSelectedOib] = useState<string | null>(null);

  const { data, isLoading, isError, error } = useMerchants(filters);

  const handlePageChange = (page: number) => {
    setFilters((p) => ({ ...p, page }));
  };

  const handleStatusChange = (status?: MerchantStatus) => {
    setFilters((p) => ({ ...p, page: 0, status }));
  };

  const handleSortChange = (sort: string) => {
    setFilters((p) => ({ ...p, page: 0, sort }));
  };

  if (isError) return <p>{getApiError(error)}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <select
        value={filters.status ?? ""}
        onChange={(e) =>
          handleStatusChange((e.target.value as MerchantStatus) || undefined)
        }
      >
        <option value="">{t("filters.all")}</option>
        <option value="active">{t("status.active")}</option>
        <option value="deactivated">{t("status.deactivated")}</option>
        <option value="hold">{t("status.hold")}</option>
      </select>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <MerchantTable
          merchants={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          currentSort={filters.sort}
          onPageChange={handlePageChange}
          onSelect={(oib) => setSelectedOib(oib)}
          onSort={handleSortChange}
        />
      )}

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
