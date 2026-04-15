import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/shared/hooks/useAuth";
import { ROUTES } from "@/router/routes";

interface RoleRouteProps {
  allowedRoles: string[];
}

export const RoleRoute = ({ allowedRoles }: RoleRouteProps) => {
  const { user, isLoading } = useAuth();

  if (isLoading) return <h2>Loading</h2>;

  if (!user) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  if (!allowedRoles.includes(user.role)) {
    return <Navigate to={ROUTES.MERCHANTS} replace />;
  }

  return <Outlet />;
};
