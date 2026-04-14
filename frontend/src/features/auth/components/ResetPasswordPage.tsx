import { useTranslation } from "react-i18next";
import { AuthForm } from "./AuthForm";

export const ResetPasswordPage = () => {
  const { t } = useTranslation("auth");

  return (
    <AuthForm
      title={t("resetPassword.title")}
      subtitle={t("resetPassword.subtitle")}
    >
      <input type="password" placeholder={t("resetPassword.newPassword")} />
      <input type="password" placeholder={t("resetPassword.confirmPassword")} />

      <button type="submit">{t("resetPassword.submit")}</button>
    </AuthForm>
  );
};
