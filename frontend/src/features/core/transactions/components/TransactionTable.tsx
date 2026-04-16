import { useTranslation } from "react-i18next";
import type { Transaction } from "../types/Transaction";
import { getPages } from "@/shared/components/getPages";

interface Props {
  transactions: Transaction[];
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onSelect: (transactionId: string) => void;
}

export const TransactionTable = ({
  transactions,
  currentPage,
  totalPages,
  onPageChange,
  onSelect,
}: Props) => {
  const { t } = useTranslation("transactions");
  const pages = getPages(currentPage, totalPages);

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>{t("fields.transactionId")}</th>
            <th>{t("fields.transactionType")}</th>
            <th>{t("fields.amount")}</th>
            <th>{t("fields.currency")}</th>
            <th>{t("fields.authorizationState")}</th>
            <th>{t("fields.cardBrand")}</th>
            <th>{t("fields.timestamp")}</th>
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {transactions.map((tx) => (
            <tr key={tx.transactionId}>
              <td>{tx.transactionId}</td>
              <td>{t(`transactionType.${tx.transactionType}`)}</td>
              <td>{tx.amount}</td>
              <td>{tx.currency}</td>
              <td>{t(`status.${tx.authorizationState}`)}</td>
              <td>{t(`cardBrand.${tx.cardBrand}`)}</td>
              <td>{new Date(tx.timestamp).toLocaleString()}</td>
              <td>
                <button onClick={() => onSelect(tx.transactionId)}>
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
