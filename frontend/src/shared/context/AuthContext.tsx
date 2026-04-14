import { createContext, useEffect, useState } from "react";
import apiClient from "@/shared/api/axios";
import type { User } from "@/shared/types/User";

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  firstLogin: boolean;
  setUser: (user: User | null) => void;
  setFirstLogin: (value: boolean) => void;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [firstLogin, setFirstLogin] = useState(false);

  useEffect(() => {
    const fetchMe = async () => {
      try {
        const { data } = await apiClient.get("/api/v1/users/me");
        setUser(data);
      } catch {
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    fetchMe();
  }, []);

  const logout = async () => {
    try {
      await apiClient.post("/api/v1/auth/logout");
    } finally {
      setUser(null);
      setFirstLogin(false);
    }
  };

  return (
    <AuthContext.Provider
      value={{ user, isLoading, firstLogin, setUser, setFirstLogin, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthContext };
