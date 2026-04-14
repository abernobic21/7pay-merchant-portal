import { useTranslation } from "react-i18next";
import { AuthForm } from "./AuthForm";

export const SetPasswordPage = () => {
  const { t } = useTranslation("auth");

  return (
    <AuthForm
      title={t("setPassword.title")}
      subtitle={t("setPassword.subtitle")}
    >
      <input type="password" placeholder={t("setPassword.newPassword")} />
      <input type="password" placeholder={t("setPassword.confirmPassword")} />

      <button type="submit">{t("setPassword.submit")}</button>
    </AuthForm>
  );
};
