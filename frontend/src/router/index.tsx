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
import { RoleRoute } from "./RoleRoute";
import { UsersPage } from "@/features/users/components/UsersPage";
import { ProfilePage } from "@/features/users/components/ProfilePage";
import { DashboardPage } from "@/features/core/dashboard/components/DashboardPage";
import { TerminalsPage } from "@/features/core/terminals/components/TerminalPage";
import { TransactionsPage } from "@/features/core/transactions/components/TransactionPage";

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
          { index: true, element: <Navigate to={ROUTES.DASHBOARD} replace /> },

          { path: ROUTES.DASHBOARD, element: <DashboardPage /> },
          { path: ROUTES.MERCHANTS, element: <MerchantsPage /> },
          { path: ROUTES.SALEPOINTS, element: <SalepointsPage /> },
          { path: ROUTES.TERMINALS, element: <TerminalsPage /> },
          { path: ROUTES.TRANSACTIONS, element: <TransactionsPage /> },
          { path: ROUTES.PROFILE, element: <ProfilePage /> },

          //ADMIN ONLY
          {
            element: <RoleRoute allowedRoles={["admin"]} />,
            children: [{ path: ROUTES.USERS, element: <UsersPage /> }],
          },
        ],
      },
    ],
  },
]);
