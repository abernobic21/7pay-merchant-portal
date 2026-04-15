import { NavLink } from "react-router-dom";
import { ROUTES } from "@/router/routes";
import { useAuth } from "@/shared/hooks/useAuth";

export const Sidebar = () => {
  const { user } = useAuth();

  return (
    <nav>
      <NavLink to={ROUTES.MERCHANTS}>Merchants</NavLink>
      <NavLink to={ROUTES.SALEPOINTS}>Salepoints</NavLink>

      {user?.role === "admin" && <NavLink to={ROUTES.USERS}>Users</NavLink>}
    </nav>
  );
};
