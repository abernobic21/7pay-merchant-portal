import { useTranslation } from "react-i18next";
import type { Salepoint } from "../types/Salepoint";

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

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>{t("fields.name")}</th>
            <th>{t("fields.mid")}</th>
            <th>{t("fields.status")}</th>
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {salepoints.map((s) => (
            <tr key={s.mid}>
              <td>{s.name}</td>
              <td>{s.mid}</td>
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
          ←
        </button>

        {Array.from({ length: totalPages }, (_, i) => (
          <button key={i} onClick={() => onPageChange(i)}>
            {i + 1}
          </button>
        ))}

        <button
          disabled={currentPage === totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          →
        </button>
      </div>
    </>
  );
};
