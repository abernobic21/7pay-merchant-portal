import { useTranslation } from "react-i18next";
import type { Merchant } from "../types/Merchant";

interface MerchantTableProps {
  merchants: Merchant[];
}

export const MerchantTable = ({ merchants }: MerchantTableProps) => {
  const { t } = useTranslation("merchants");
  return (
    <table>
      <thead>
        <tr>
          <th>{t("fields.shortName")}</th>
          <th>{t("fields.fullName")}</th>
          <th>{t("fields.oib")}</th>
          <th>{t("fields.address")}</th>
          <th>{t("fields.status")}</th>
        </tr>
      </thead>
      <tbody>
        {merchants.map((merchant) => (
          <tr key={merchant.oib}>
            <td>{merchant.shortName}</td>
            <td>{merchant.fullName}</td>
            <td>{merchant.oib}</td>
            <td>
              {merchant.address.streetAddressNumber +
                " " +
                merchant.address.city +
                ", " +
                merchant.address.zipCode +
                ", " +
                merchant.address.country}
            </td>
            <td>{merchant.status}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
