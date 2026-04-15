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
      <NavLink to={ROUTES.MERCHANTS}>Merchants</NavLink>
      <NavLink to={ROUTES.SALEPOINTS}>Salepoints</NavLink>

      {user?.role === "admin" && <NavLink to={ROUTES.USERS}>Users</NavLink>}

      <NavLink to={ROUTES.PROFILE}>Profile</NavLink>
      <div>
        <button onClick={handleLogout}>{t("logout")}</button>
      </div>
    </nav>
  );
};
