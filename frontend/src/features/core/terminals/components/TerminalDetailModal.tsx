import { useTranslation } from "react-i18next";
import { useTerminalDetail } from "../hooks/useTerminalDetail";
import type { AcquirerTerminalData } from "../types/Terminal";

interface Props {
  tid: string;
  onClose: () => void;
}

export const TerminalDetailModal = ({ tid, onClose }: Props) => {
  const { t } = useTranslation(["terminals", "transactions"]);
  const { data, isLoading, error } = useTerminalDetail(tid);

  if (isLoading) return <p>{t("loading")}</p>;
  if (error || !data) return <p>{t("error")}</p>;

  return (
    <dialog open>
      <h2>{data.tid}</h2>

      <p>
        <b>{t("details.terminalType")}:</b> {data.terminalType}
      </p>
      <p>
        <b>{t("details.status")}:</b> {t(`status.${data.status.toLowerCase()}`)}
      </p>
      <p>
        <b>{t("details.midReference")}:</b> {data.midReference}
      </p>
      <p>
        <b>{t("details.currency")}:</b> {data.currency}
      </p>
      <p>
        <b>{t("details.phoneNumber")}:</b> {data.phoneNumber}
      </p>
      <p>
        <b>{t("details.emailContact")}:</b> {data.emailContact}
      </p>
      <p>
        <b>{t("details.authenticationMode")}:</b> {data.authenticationMode}
      </p>

      <p>
        <b>{t("details.tipEnabled")}:</b>{" "}
        {data.tipEnabled ? t("common:yes") : t("common:no")}
      </p>

      <h3>{t("details.acquirers")}</h3>

      {data.acquirerTerminalData.map((a: AcquirerTerminalData) => (
        <div key={a.acquirerTid}>
          <p>
            <b>{t("details.acquirerHost")}:</b> {a.acquirerHost}
          </p>
          <p>
            <b>{t("details.acquirerTid")}:</b> {a.acquirerTid}
          </p>

          <p>{t("details.transactionTypes")}:</p>
          <ul>
            {a.transactionTypes.map((tType) => (
              <li key={tType}>
                {" "}
                {t(`transactions:transactionTypes.${tType}`)}
              </li>
            ))}
          </ul>

          <p>{t("details.cardBrands")}:</p>
          <ul>
            {a.cardBrands.map((b) => (
              <li key={b}> {t(`transactions:cardBrands.${b}`)}</li>
            ))}
          </ul>
        </div>
      ))}

      <button onClick={onClose}>{t("actions.close")}</button>
    </dialog>
  );
};
