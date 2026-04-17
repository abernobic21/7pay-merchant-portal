import { useTranslation } from "react-i18next";
import { useTransactionDetail } from "../hooks/useTransactionDetail";

interface Props {
  transactionId: string;
  onClose: () => void;
}

export const TransactionDetailModal = ({ transactionId, onClose }: Props) => {
  const { t } = useTranslation("transactions");
  const { data, isLoading, error } = useTransactionDetail(transactionId);

  if (isLoading) return <p>{t("loading")}</p>;
  if (error || !data) return <p>{t("error")}</p>;

  return (
    <dialog open>
      <h2>{data.transactionId}</h2>

      <p>
        <b>{t("details.transactionType")}:</b>{" "}
        {t(`transactionTypes.${data.transactionType}`)}
      </p>
      <p>
        <b>{t("details.amount")}:</b> {data.amount} {data.currency}
      </p>
      <p>
        <b>{t("details.authorizationState")}:</b>{" "}
        {t(`status.${data.authorizationState}`)}
      </p>
      <p>
        <b>{t("details.validity")}:</b> {t(`validity.${data.validity}`)}
      </p>
      <p>
        <b>{t("details.responseCode")}:</b> {data.responseCode}
      </p>
      <p>
        <b>{t("details.authNumber")}:</b> {data.authNumber}
      </p>
      <p>
        <b>{t("details.rrn")}:</b> {data.rrn}
      </p>
      <p>
        <b>{t("details.acquirerRrn")}:</b> {data.acquirerRrn}
      </p>
      <p>
        <b>{t("details.stan")}:</b> {data.stan}
      </p>
      <p>
        <b>{t("details.acquirerStan")}:</b> {data.acquirerStan}
      </p>
      <p>
        <b>{t("details.cardBrand")}:</b> {t(`cardBrands.${data.cardBrand}`)}
      </p>
      <p>
        <b>{t("details.panMasked")}:</b> {data.panMasked}
      </p>
      <p>
        <b>{t("details.panExpireDate")}:</b> {data.panExpireDate}
      </p>
      <p>
        <b>{t("details.acquirerHost")}:</b> {data.acquirerHost}
      </p>
      <p>
        <b>{t("details.acquirerTid")}:</b> {data.acquirerTid}
      </p>
      <p>
        <b>{t("details.acquirerMid")}:</b> {data.acquirerMid}
      </p>
      <p>
        <b>{t("details.tid")}:</b> {data.tid}
      </p>
      <p>
        <b>{t("details.mid")}:</b> {data.mid}
      </p>
      <p>
        <b>{t("details.paymentType")}:</b> {data.paymentType}
      </p>

      {data.installmentNumber != null && (
        <p>
          <b>{t("details.installmentNumber")}:</b> {data.installmentNumber}
        </p>
      )}
      {data.tipAmount != null && (
        <p>
          <b>{t("details.tipAmount")}:</b> {data.tipAmount} {data.currency}
        </p>
      )}
      {data.amount2 != null && (
        <p>
          <b>{t("details.amount2")}:</b> {data.amount2} {data.currency2}
        </p>
      )}

      <p>
        <b>{t("details.timestamp")}:</b>{" "}
        {new Date(data.timestamp).toLocaleString()}
      </p>
      <p>
        <b>{t("details.posTimestamp")}:</b>{" "}
        {new Date(data.posTimestamp).toLocaleString()}
      </p>
      <p>
        <b>{t("details.acquirerTimestamp")}:</b>{" "}
        {new Date(data.acquirerTimestamp).toLocaleString()}
      </p>
      <p>
        <b>{t("details.transactionReference")}:</b> {data.transactionReference}
      </p>
      <p>
        <b>{t("details.transactionStateMDCProcessing")}:</b>{" "}
        {t(
          `transactionStateMDCProcessing.${data.transactionStateMDCProcessing}`,
        )}
      </p>

      <button onClick={onClose}>{t("actions.close")}</button>
    </dialog>
  );
};
