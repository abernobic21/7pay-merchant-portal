import { createContext, useEffect, useState } from "react";
import apiClient from "@/shared/api/apiClient";
import type { UserProfile } from "@/features/users/types/User";

interface AuthContextType {
  user: UserProfile | null;
  isLoading: boolean;
  setUser: (user: UserProfile | null) => void;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const isAuthPage =
    location.pathname.includes("login") ||
    location.pathname.includes("set-password") ||
    location.pathname.includes("reset-password") ||
    location.pathname.includes("forgot-password");

  useEffect(() => {
    if (isAuthPage) {
      setIsLoading(false);
      return;
    }

    apiClient
      .get<UserProfile>("/users/me")
      .then(({ data }) => setUser(data))
      .catch(() => setUser(null))
      .finally(() => setIsLoading(false));
  }, [isAuthPage]);

  const logout = async () => {
    try {
      await apiClient.post("/auth/logout");
    } finally {
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider value={{ user, isLoading, setUser, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export { AuthContext };
