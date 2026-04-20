import { useTranslation } from "react-i18next";
import type { Merchant } from "../types/Merchant";
import { getPages } from "@/shared/components/getPages";
import { SortableHeader } from "@/shared/components/SortableHeader";

interface Props {
  merchants: Merchant[];
  currentPage: number;
  currentSort?: string;
  totalPages: number;
  onPageChange: (page: number) => void;
  onSelect: (oib: string) => void;
  onSort: (sort: string) => void;
}

export const MerchantTable = ({
  merchants,
  currentPage,
  currentSort,
  totalPages,
  onPageChange,
  onSelect,
  onSort,
}: Props) => {
  const { t } = useTranslation("merchants");
  const pages = getPages(currentPage, totalPages);

  return (
    <>
      <table>
        <thead>
          <tr>
            <SortableHeader
              label={t("fields.shortName")}
              field="shortName"
              currentSort={currentSort}
              onSort={onSort}
            />{" "}
            <th>{t("fields.fullName")}</th>
            <th>{t("fields.oib")}</th>
            <th>{t("fields.status")}</th>
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {merchants.map((m) => (
            <tr key={m.oib}>
              <td>{m.shortName}</td>
              <td>{m.fullName}</td>
              <td>{m.oib}</td>
              <td>{t(`status.${m.status.toLowerCase()}`)}</td>
              <td>
                <button onClick={() => onSelect(m.oib)}>
                  {t("actions.details")}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div>
        <button
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
        >
          {t("pagination.previous")}
        </button>

        {pages.map((p, i) =>
          p === "..." ? (
            <span key={i}>...</span>
          ) : (
            <button
              key={p}
              disabled={p === currentPage}
              onClick={() => onPageChange(p as number)}
            >
              {(p as number) + 1}
            </button>
          ),
        )}

        <button
          disabled={currentPage === totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          {t("pagination.next")}
        </button>
      </div>
    </>
  );
};
