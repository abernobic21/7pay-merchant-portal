import { useTranslation } from "react-i18next";
import type { Terminal } from "../types/Terminal";
import { getPages } from "@/shared/components/getPages";
import { SortableHeader } from "@/shared/components/SortableHeader";

interface Props {
  terminals: Terminal[];
  currentPage: number;
  totalPages: number;
  currentSort?: string;
  onPageChange: (page: number) => void;
  onSelect: (tid: string) => void;
  onSort: (sort: string) => void;
}

export const TerminalTable = ({
  terminals,
  currentPage,
  totalPages,
  currentSort,
  onPageChange,
  onSelect,
  onSort,
}: Props) => {
  const { t } = useTranslation("terminals");
  const pages = getPages(currentPage, totalPages);

  return (
    <>
      <table>
        <thead>
          <tr>
            <SortableHeader
              label={t("fields.tid")}
              field="tid"
              currentSort={currentSort}
              onSort={onSort}
            />
            <th>{t("fields.terminalType")}</th>
            <th>{t("fields.status")}</th>
            <th>{t("fields.midReference")}</th>
            <th>{t("fields.currency")}</th>
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {terminals.map((tItem) => (
            <tr key={tItem.tid}>
              <td>{tItem.tid}</td>
              <td>{tItem.terminalType}</td>
              <td>{t(`status.${tItem.status.toLowerCase()}`)}</td>
              <td>{tItem.midReference}</td>
              <td>{tItem.currency}</td>
              <td>
                <button onClick={() => onSelect(tItem.tid)}>
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
