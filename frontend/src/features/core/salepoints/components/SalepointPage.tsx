import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useSalepoints } from "../hooks/useSalepoints";
import { SalepointTable } from "./SalepointTable";
import { SalepointDetailModal } from "./SalepointDetailModal";

export const SalepointsPage = () => {
  const { t } = useTranslation("salepoints");

  const [filters, setFilters] = useState({
    page: 0,
    size: 10,
  });

  const [selectedMid, setSelectedMid] = useState<string | null>(null);

  const { data, isLoading, isError } = useSalepoints(filters);

  if (isError) return <p>{t("error")}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : (
        <SalepointTable
          salepoints={data?.content ?? []}
          currentPage={filters.page}
          totalPages={data?.page.totalPages ?? 0}
          onPageChange={(page) => setFilters((p) => ({ ...p, page }))}
          onSelect={(mid) => setSelectedMid(mid)}
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
