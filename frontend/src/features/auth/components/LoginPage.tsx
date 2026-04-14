import { Link } from "react-router-dom";
import { AuthForm } from "./AuthForm";

import { useTranslation } from "react-i18next";

export const LoginPage = () => {
  const { t } = useTranslation("auth");

  return (
    <AuthForm
      title={t("login.title")}
      footer={<Link to="/forgot-password">{t("login.forgotPassword")}</Link>}
    >
      <input type="email" placeholder={t("login.email")} />
      <input type="password" placeholder={t("login.password")} />

      <button type="submit">{t("login.submit")}</button>
    </AuthForm>
  );
};
