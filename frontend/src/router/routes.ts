export const ROUTES = {
  // public
  LOGIN: "/login",
  SET_PASSWORD: "/set-password",
  FORGOT_PASSWORD: "/forgot-password",
  RESET_PASSWORD: "/reset-password",

  // protected
  USERS: "/users",
  DASHBOARD: "/dashboard",
  MERCHANTS: "/merchants",
  SALEPOINTS: "/salepoints",
  TERMINALS: "/terminals",
  TRANSACTIONS: "/transactions",
  PROFILE: "/profile",
} as const;
