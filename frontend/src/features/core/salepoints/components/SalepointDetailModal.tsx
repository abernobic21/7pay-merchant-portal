import { useTranslation } from "react-i18next";
import { useSalepointDetail } from "../hooks/useSalepointDetail";
import type { AcquirerMerchantData } from "../types/Salepoint";

interface Props {
  mid: string;
  onClose: () => void;
}

export const SalepointDetailModal = ({ mid, onClose }: Props) => {
  const { t } = useTranslation(["salepoints", "transactions"]);
  const { data, isLoading, error } = useSalepointDetail(mid);

  if (isLoading) return <div>{t("loading")}</div>;
  if (error || !data) return <div>{t("error")}</div>;

  return (
    <dialog open>
      <h2>{t("details.title")}</h2>

      <p>
        <b>{t("details.name")}:</b> {data.name}
      </p>

      <p>
        <b>{t("details.mid")}:</b> {data.mid}
      </p>

      <p>
        <b>{t("details.status")}:</b> {t(`status.${data.status.toLowerCase()}`)}
      </p>

      <p>
        <b>{t("details.merchantOib")}:</b> {data.merchantOib}
      </p>

      <h3>{t("details.address")}</h3>
      <p>
        {data.address.streetAddressNumber}, {data.address.city}{" "}
        {data.address.zipCode}, {data.address.country}
      </p>

      <h3>{t("details.acquirers")}</h3>

      {data.acquirerMerchantData.map((a: AcquirerMerchantData) => (
        <div key={a.acquirerMid}>
          <p>
            <b>{t("details.acquirerHost")}:</b> {a.acquirerHost}
          </p>

          <p>
            <b>{t("details.acquirerMid")}:</b> {a.acquirerMid}
          </p>

          <p>
            <b>{t("details.transactionTypes")}:</b>
          </p>
          <ul>
            {a.transactionTypes.map((type) => (
              <li key={type}>{t(`transactions:transactionTypes.${type}`)}</li>
            ))}
          </ul>

          <p>
            <b>{t("details.cardBrands")}:</b>
          </p>
          <ul>
            {a.cardBrands.map((b) => (
              <li key={b}>{t(`transactions:cardBrands.${b}`)}</li>
            ))}
          </ul>
        </div>
      ))}

      <button onClick={onClose}>{t("actions.close")}</button>
    </dialog>
  );
};
