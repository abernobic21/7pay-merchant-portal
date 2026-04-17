import { useTranslation } from "react-i18next";
import type { Salepoint } from "../types/Salepoint";
import { getPages } from "@/shared/components/getPages";

interface Props {
  salepoints: Salepoint[];
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onSelect: (mid: string) => void;
}

export const SalepointTable = ({
  salepoints,
  currentPage,
  totalPages,
  onPageChange,
  onSelect,
}: Props) => {
  const { t } = useTranslation("salepoints");
  const pages = getPages(currentPage, totalPages);

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>{t("fields.name")}</th>
            <th>{t("fields.mid")}</th>
            <th>{t("fields.merchantOib")}</th>
            <th>{t("fields.status")}</th>
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {salepoints.map((s) => (
            <tr key={s.mid}>
              <td>{s.name}</td>
              <td>{s.mid}</td>
              <td>{s.merchantOib}</td>
              <td>{t(`status.${s.status.toLowerCase()}`)}</td>
              <td>
                <button onClick={() => onSelect(s.mid)}>
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
