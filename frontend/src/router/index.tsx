import { createBrowserRouter, Navigate } from "react-router-dom";
import { MainLayout } from "@/layouts/MainLayout";
import { MerchantsPage } from "@/features/core/merchants/components/MerchantPage";
import { ROUTES } from "./routes";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        index: true,
        element: <Navigate to={ROUTES.MERCHANTS} replace />,
      },
      {
        path: ROUTES.MERCHANTS,
        element: <MerchantsPage />,
      },
    ],
  },
]);
