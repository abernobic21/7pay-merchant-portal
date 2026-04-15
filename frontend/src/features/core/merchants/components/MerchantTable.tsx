import { useTranslation } from "react-i18next";
import type { Merchant } from "../types/Merchant";

interface Props {
  merchants: Merchant[];
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onSelect: (oib: string) => void;
}

export const MerchantTable = ({
  merchants,
  currentPage,
  totalPages,
  onPageChange,
  onSelect,
}: Props) => {
  const { t } = useTranslation("merchants");

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>{t("fields.shortName")}</th>
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
