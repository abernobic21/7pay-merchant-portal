import { AuthForm } from "./AuthForm";
import { useTranslation } from "react-i18next";

export const ForgotPasswordPage = () => {
  const { t } = useTranslation("auth");

  return (
    <AuthForm
      title={t("forgotPassword.title")}
      subtitle={t("forgotPassword.subtitle")}
    >
      <input type="email" placeholder={t("forgotPassword.email")} />

      <button type="submit">{t("forgotPassword.submit")}</button>
    </AuthForm>
  );
};
