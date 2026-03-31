import { useTranslation } from "react-i18next";
import type { Salepoint } from "../types/Salepoint";

interface SalepointTableProps {
  salepoints: Salepoint[];
}

export const SalepointTable = ({ salepoints }: SalepointTableProps) => {
  const { t } = useTranslation("salepoints");
  return (
    <table>
      <thead>
        <tr>
          <th>{t("fields.name")}</th>
          <th>{t("fields.mid")}</th>
          <th>{t("fields.merchantOib")}</th>
          <th>{t("fields.address")}</th>
          <th>{t("fields.status")}</th>
        </tr>
      </thead>
      <tbody>
        {salepoints.map((salepoint) => (
          <tr key={salepoint.name}>
            <td>{salepoint.name}</td>
            <td>{salepoint.mid}</td>
            <td>{salepoint.merchantOib}</td>
            <td>
              {salepoint.address.streetAddressNumber +
                ", " +
                salepoint.address.city +
                " " +
                salepoint.address.zipCode +
                ", " +
                salepoint.address.country}
            </td>
            <td>{salepoint.status}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};
