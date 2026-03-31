import { useTranslation } from "react-i18next";
import { MerchantTable } from "../components/MerchantTable";
import { useMerchants } from "../hooks/useMerschants";

export const MerchantsPage = () => {
  const { t } = useTranslation("merchants");
  const { data, isLoading, isError } = useMerchants();

  if (isLoading) return <div>{t("loading")}</div>;
  if (isError) return <div>{t("error")}</div>;

  return (
    <div>
      <h1>{t("title")}</h1>
      <MerchantTable merchants={data?.content ?? []} />
    </div>
  );
};
