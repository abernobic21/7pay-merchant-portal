import { useTranslation } from "react-i18next";
import type { Transaction } from "../types/Transaction";
import { getPages } from "@/shared/components/getPages";
import { SortableHeader } from "@/shared/components/SortableHeader";

interface Props {
  transactions: Transaction[];
  currentPage: number;
  totalPages: number;
  currentSort?: string;
  onPageChange: (page: number) => void;
  onSelect: (transactionId: string) => void;
  onSort: (sort: string) => void;
}

export const TransactionTable = ({
  transactions,
  currentPage,
  totalPages,
  currentSort,
  onPageChange,
  onSelect,
  onSort,
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
            <SortableHeader
              label={t("fields.amount")}
              field="amount"
              currentSort={currentSort}
              onSort={onSort}
            />
            <th>{t("fields.currency")}</th>
            <th>{t("fields.authorizationState")}</th>
            <th>{t("fields.cardBrand")}</th>
            <SortableHeader
              label={t("fields.timestamp")}
              field="timestamp"
              currentSort={currentSort}
              onSort={onSort}
            />
            <th>{t("fields.actions")}</th>
          </tr>
        </thead>

        <tbody>
          {transactions.map((tx) => (
            <tr key={tx.transactionId}>
              <td>{tx.transactionId}</td>
              <td>{t(`transactionTypes.${tx.transactionType}`)}</td>
              <td>{tx.amount}</td>
              <td>{tx.currency}</td>
              <td>{t(`status.${tx.authorizationState}`)}</td>
              <td>{t(`cardBrands.${tx.cardBrand}`)}</td>
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
