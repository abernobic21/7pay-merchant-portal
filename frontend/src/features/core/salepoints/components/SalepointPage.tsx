import { useTranslation } from "react-i18next";
import { SalepointTable } from "../components/SalepointTable";
import { useSalepoints } from "../hooks/useSalepoints";

export const SalepointsPage = () => {
  const { t } = useTranslation("salepoints");
  const { data, isLoading, isError } = useSalepoints();

  if (isLoading) return <div>{t("loading")}</div>;
  if (isError) return <div>{t("error")}</div>;

  return (
    <div>
      <h1>{t("title")}</h1>
      <SalepointTable salepoints={data?.content ?? []} />
    </div>
  );
};
