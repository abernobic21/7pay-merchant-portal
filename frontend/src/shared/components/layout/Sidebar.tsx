import { NavLink, useNavigate } from "react-router-dom";
import { ROUTES } from "@/router/routes";
import { useAuth } from "@/shared/hooks/useAuth";
import { useTranslation } from "react-i18next";

export const Sidebar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const { t } = useTranslation("common");

  const handleLogout = async () => {
    await logout();
    navigate(ROUTES.LOGIN);
  };

  return (
    <nav>
      <NavLink to={ROUTES.DASHBOARD}>{t("nav.dashboard")}</NavLink>
      <NavLink to={ROUTES.MERCHANTS}>{t("nav.merchants")}</NavLink>
      <NavLink to={ROUTES.SALEPOINTS}>{t("nav.salepoints")}</NavLink>
      <NavLink to={ROUTES.TERMINALS}>{t("nav.terminals")}</NavLink>
      <NavLink to={ROUTES.TRANSACTIONS}>{t("nav.transactions")}</NavLink>

      {user?.role === "admin" && (
        <NavLink to={ROUTES.USERS}>{t("nav.users")}</NavLink>
      )}

      <NavLink to={ROUTES.PROFILE}>{t("nav.profile")}</NavLink>

      <div>
        <button onClick={handleLogout}>{t("logout")}</button>
      </div>
    </nav>
  );
};
