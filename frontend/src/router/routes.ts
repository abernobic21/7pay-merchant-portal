export const ROUTES = {
  // public
  LOGIN: "/login",
  SET_PASSWORD: "/set-password",
  FORGOT_PASSWORD: "/forgot-password",
  RESET_PASSWORD: "/reset-password",

  // protected
  USERS: "/users",
  MERCHANTS: "/merchants",
  SALEPOINTS: "/salepoints",
  PROFILE: "/profile",
} as const;
