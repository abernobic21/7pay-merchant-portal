import { NavLink } from "react-router-dom";
import { ROUTES } from "@/router/routes";

export const Sidebar = () => {
  return (
    <nav>
      <NavLink to={ROUTES.MERCHANTS}>Merchants</NavLink>
    </nav>
  );
};
