import { useTranslation } from "react-i18next";

export const ChangePasswordPage = () => {
  const { t } = useTranslation("auth");

  return (
    <div className="page-container">
      <h2>{t("changePassword.title")}</h2>

      <form className="auth-form">
        <input
          type="password"
          placeholder={t("changePassword.currentPassword")}
        />
        <input type="password" placeholder={t("changePassword.newPassword")} />
        <input
          type="password"
          placeholder={t("changePassword.confirmPassword")}
        />

        <button type="submit">{t("changePassword.submit")}</button>
      </form>
    </div>
  );
};
