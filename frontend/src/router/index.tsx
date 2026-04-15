import { createBrowserRouter, Navigate } from "react-router-dom";
import { MainLayout } from "@/layouts/MainLayout";
import { AuthLayout } from "@/layouts/AuthLayout";
import { MerchantsPage } from "@/features/core/merchants/components/MerchantPage";
import { SalepointsPage } from "@/features/core/salepoints/components/SalepointPage";
import { ROUTES } from "./routes";
import { ProtectedRoute } from "./ProtectedRoute";
import { LoginPage } from "@/features/auth/components/LoginPage";
import { SetPasswordPage } from "@/features/auth/components/SetPasswordPage";
import { ForgotPasswordPage } from "@/features/auth/components/ForgotPasswordPage";
import { ResetPasswordPage } from "@/features/auth/components/ResetPasswordPage";
import { UsersPage } from "@/features/users/components/UsersPage";

export const router = createBrowserRouter([
  // public
  {
    path: "/",
    element: <AuthLayout />,
    children: [
      { path: ROUTES.LOGIN, element: <LoginPage /> },
      { path: ROUTES.SET_PASSWORD, element: <SetPasswordPage /> },
      { path: ROUTES.FORGOT_PASSWORD, element: <ForgotPasswordPage /> },
      { path: ROUTES.RESET_PASSWORD, element: <ResetPasswordPage /> },
    ],
  },
  // protected
  {
    path: "/",
    element: <ProtectedRoute />,
    children: [
      {
        element: <MainLayout />,
        children: [
          { index: true, element: <Navigate to={ROUTES.MERCHANTS} replace /> },
          { path: ROUTES.MERCHANTS, element: <MerchantsPage /> },
          { path: ROUTES.SALEPOINTS, element: <SalepointsPage /> },
          { path: ROUTES.USERS, element: <UsersPage /> },
        ],
      },
    ],
  },
]);
