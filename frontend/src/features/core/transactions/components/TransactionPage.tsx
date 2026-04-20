import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTransactions } from "../hooks/useTransactions";
import { TransactionTable } from "./TransactionTable";
import { TransactionDetailModal } from "./TransactionDetailModal";
import type { TransactionFilters } from "../types/Transaction";
import { getApiError } from "@/shared/utils/getApiError";
import { useAuth } from "@/shared/hooks/useAuth";

const toISO = (d: Date) => d.toISOString().slice(0, 16);

const defaultTo = () => toISO(new Date());
const defaultFrom = () => {
  const d = new Date();
  d.setDate(d.getDate() - 7);
  return toISO(d);
};

export const TransactionsPage = () => {
  const { t } = useTranslation("transactions");

  const { user } = useAuth();
  const defaultMerchantOib = user?.merchantId ?? "";

  const [merchantOib, setMerchantOib] = useState(defaultMerchantOib);
  const [timeFrom, setTimeFrom] = useState(defaultFrom());
  const [timeUntil, setTimeUntil] = useState(defaultTo());
  const [rangeError, setRangeError] = useState<string | null>(null);

  const [filters, setFilters] = useState<TransactionFilters>({
    merchantOib: "",
    timeFrom: defaultFrom(),
    timeUntil: defaultTo(),
    page: 0,
    size: 10,
  });

  const canSearch = !!filters.merchantOib && !rangeError;

  const { data, isLoading, isError, error } = useTransactions(
    filters,
    canSearch,
  );

  const [selectedTransactionId, setSelectedTransactionId] = useState<
    string | null
  >(null);

  const validateAndSetRange = (from: string, to: string) => {
    const diff =
      (new Date(to).getTime() - new Date(from).getTime()) /
      (1000 * 60 * 60 * 24);
    if (diff < 0) {
      setRangeError(t("filters.rangeError.toBeforeFrom"));
    } else if (diff > 31) {
      setRangeError(t("filters.rangeError.exceeds31Days"));
    } else {
      setRangeError(null);
    }
  };

  const handleTimeFrom = (value: string) => {
    setTimeFrom(value);
    validateAndSetRange(value, timeUntil);
  };

  const handleTimeUntil = (value: string) => {
    setTimeUntil(value);
    validateAndSetRange(timeFrom, value);
  };

  const handleSearch = () => {
    if (!merchantOib) return;
    setFilters((prev) => ({
      ...prev,
      merchantOib,
      timeFrom,
      timeUntil: timeUntil,
      page: 0,
    }));
  };

  const handleChange = (field: keyof TransactionFilters, value: string) => {
    setFilters((prev) => ({
      ...prev,
      page: 0,
      [field]: value || undefined,
    }));
  };

  const handlePageChange = (page: number) => {
    setFilters((prev) => ({ ...prev, page }));
  };

  const handleSortChange = (sort: string) => {
    setFilters((prev) => ({ ...prev, page: 0, sort }));
  };

  if (isError) return <p>{getApiError(error)}</p>;

  return (
    <div>
      <h1>{t("title")}</h1>

      {/* Required filters */}
      <div>
        <input
          placeholder={t("filters.merchantOib")}
          value={merchantOib}
          onChange={(e) => setMerchantOib(e.target.value)}
        />

        <input
          type="datetime-local"
          value={timeFrom}
          onChange={(e) => handleTimeFrom(e.target.value)}
        />

        <input
          type="datetime-local"
          value={timeUntil}
          onChange={(e) => handleTimeUntil(e.target.value)}
        />

        {rangeError && <p>{rangeError}</p>}

        <button onClick={handleSearch} disabled={!merchantOib || !!rangeError}>
          {t("actions.search")}
        </button>
      </div>

      {/* Optional filters */}
      <div>
        <input
          placeholder={t("filters.mid")}
          value={filters.mid ?? ""}
          onChange={(e) => handleChange("mid", e.target.value)}
        />

        <input
          placeholder={t("filters.tid")}
          value={filters.tid ?? ""}
          onChange={(e) => handleChange("tid", e.target.value)}
        />

        <input
          placeholder={t("filters.authNumber")}
          value={filters.authNumber ?? ""}
          onChange={(e) => handleChange("authNumber", e.target.value)}
        />

        <input
          placeholder={t("filters.rrn")}
          value={filters.rrn ?? ""}
          onChange={(e) => handleChange("rrn", e.target.value)}
        />

        <select
          value={filters.status ?? ""}
          onChange={(e) => handleChange("status", e.target.value)}
        >
          <option value="">{t("filters.status")}</option>
          <option value="authorized">{t("status.authorized")}</option>
          <option value="rejected">{t("status.rejected")}</option>
          <option value="no_response">{t("status.no_response")}</option>
        </select>

        <select
          value={filters.transactionType ?? ""}
          onChange={(e) => handleChange("transactionType", e.target.value)}
        >
          <option value="">{t("filters.transactionType")}</option>
          <option value="sale">{t("transactionTypes.sale")}</option>
          <option value="sale_with_installments">
            {t("transactionTypes.sale_with_installments")}
          </option>
          <option value="refund">{t("transactionTypes.refund")}</option>
          <option value="storno">{t("transactionTypes.storno")}</option>
          <option value="technical_reversal">
            {t("transactionTypes.technical_reversal")}
          </option>
        </select>

        <select
          value={filters.cardBrand ?? ""}
          onChange={(e) => handleChange("cardBrand", e.target.value)}
        >
          <option value="">{t("filters.cardBrand")}</option>
          <option value="mastercard">{t("cardBrands.mastercard")}</option>
          <option value="visa">{t("cardBrands.visa")}</option>
          <option value="amex">{t("cardBrands.amex")}</option>
          <option value="maestro">{t("cardBrands.maestro")}</option>
          <option value="diners">{t("cardBrands.diners")}</option>
          <option value="discover">{t("cardBrands.discover")}</option>
          <option value="jcb">{t("cardBrands.jcb")}</option>
          <option value="naka">{t("cardBrands.naka")}</option>
          <option value="closed_loop_prepaid">
            {t("cardBrands.closed_loop_prepaid")}
          </option>
          <option value="closed_loop_postpaid">
            {t("cardBrands.closed_loop_postpaid")}
          </option>
        </select>
      </div>

      {isLoading ? (
        <p>{t("loading")}</p>
      ) : canSearch && data ? (
        <TransactionTable
          transactions={data.content ?? []}
          currentPage={filters.page}
          totalPages={data.page.totalPages ?? 0}
          currentSort={filters.sort}
          onPageChange={handlePageChange}
          onSelect={setSelectedTransactionId}
          onSort={handleSortChange}
        />
      ) : null}

      {selectedTransactionId && (
        <TransactionDetailModal
          key={selectedTransactionId}
          transactionId={selectedTransactionId}
          onClose={() => setSelectedTransactionId(null)}
        />
      )}
    </div>
  );
};
