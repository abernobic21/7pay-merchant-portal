import { Outlet } from "react-router-dom";
import { Sidebar } from "@/shared/components/layout/Sidebar";
import { LanguageSwitcher } from "@/shared/components/LanguageSwitcher";

export const MainLayout = () => {
  return (
    <div>
      <Sidebar />
      <main>
        <div>
          <LanguageSwitcher />
        </div>
        <Outlet />
      </main>
    </div>
  );
};
