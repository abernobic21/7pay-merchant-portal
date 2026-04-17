import { useTranslation } from "react-i18next";
import { useMerchantDetail } from "../hooks/useMerchantDetail";

interface Props {
  oib: string;
  onClose: () => void;
}

export const MerchantDetailModal = ({ oib, onClose }: Props) => {
  const { t } = useTranslation("merchants");
  const { data, isLoading, error } = useMerchantDetail(oib);

  if (isLoading) return <div>{t("loading")}</div>;
  if (error || !data) return <div>{t("error")}</div>;

  return (
    <dialog open>
      <h2>{t("details.title")}</h2>

      <p>
        <b>{t("details.shortName")}:</b> {data.shortName}
      </p>

      <p>
        <b>{t("details.oib")}:</b> {data.oib}
      </p>

      <p>
        <b>{t("details.status")}:</b> {t(`status.${data.status.toLowerCase()}`)}
      </p>

      <h3>{t("details.address")}</h3>
      <p>
        {data.address.streetAddressNumber}, {data.address.city}{" "}
        {data.address.zipCode}, {data.address.country}
      </p>

      <h3>{t("details.transactionTypes")}</h3>
      <ul>
        {data.supportedTransactionTypes.map((type) => (
          <li key={type}>
            {t(`transactionTypes.${type.toLowerCase()}`, type)}
          </li>
        ))}
      </ul>

      <h3>{t("details.acquirers")}</h3>
      <ul>
        {data.supportedAcquirers.map((a) => (
          <li key={a}>{a}</li>
        ))}
      </ul>

      <h3>{t("details.cardBrands")}</h3>
      <ul>
        {data.supportedCardBrands.map((b) => (
          <li key={b}>{t(`cardBrands.${b.toLowerCase()}`, b)}</li>
        ))}
      </ul>

      <button onClick={onClose}>{t("actions.close")}</button>
    </dialog>
  );
};
