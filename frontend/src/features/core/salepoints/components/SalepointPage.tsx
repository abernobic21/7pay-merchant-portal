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

  const handleChange = (field: keyof SalepointFilters, value: string) => {
    setFilters((p) => ({ ...p, page: 0, [field]: value || undefined }));
  };

  if (isError) return <p>{getApiError(error, "salepoints")}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      <div>
        <input
          placeholder={t("filters.merchantOib")}
          value={filters.merchantOib ?? ""}
          onChange={(e) => handleChange("merchantOib", e.target.value)}
        />
      </div>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <SalepointTable
          salepoints={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          onPageChange={(page) => setFilters((p) => ({ ...p, page }))}
          onSelect={setSelectedMid}
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
