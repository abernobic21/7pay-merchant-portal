export const ROUTES = {
  // public
  LOGIN: "/login",
  SET_PASSWORD: "/set-password",
  FORGOT_PASSWORD: "/forgot-password",
  RESET_PASSWORD: "/reset-password",

  // protected
  CHANGE_PASSWORD: "/change-password",
  MERCHANTS: "/merchants",
  SALEPOINTS: "/salepoints",
} as const;
