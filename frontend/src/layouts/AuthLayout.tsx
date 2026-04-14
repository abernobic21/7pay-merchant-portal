import { LanguageSwitcher } from "@/shared/components/LanguageSwitcher";
import { Outlet } from "react-router-dom";

export const AuthLayout = () => {
  return (
    <div>
      <div>
        <h1>Merchant portal</h1>
        <LanguageSwitcher />
      </div>

      <div>
        <Outlet />
      </div>
    </div>
  );
};
