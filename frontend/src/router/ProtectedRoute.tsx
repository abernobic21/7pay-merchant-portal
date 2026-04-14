import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/shared/hooks/useAuth";
import { ROUTES } from "@/router/routes";

export const ProtectedRoute = () => {
  const { user, isLoading, firstLogin } = useAuth();

  if (isLoading) return <h2>Loading</h2>;
  if (!user) return <Navigate to={ROUTES.LOGIN} replace />;
  if (firstLogin) return <Navigate to={ROUTES.CHANGE_PASSWORD} replace />;

  return <Outlet />;
};
