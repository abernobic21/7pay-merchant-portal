import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { useLogin } from "../hooks/useLogin";
import { ROUTES } from "@/router/routes";
import { AuthForm } from "./AuthForm";
import type { AxiosError } from "axios";

export const LoginPage = () => {
  const { t } = useTranslation("auth");
  const { mutate, isPending, error } = useLogin();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = new FormData(e.currentTarget);

    const email = form.get("email") as string;
    const password = form.get("password") as string;

    mutate({ email, password });
  };

  return (
    <AuthForm
      title={t("login.title")}
      onSubmit={handleSubmit}
      isLoading={isPending}
      submitLabel={t("login.submit")}
      footer={
        <Link to={ROUTES.FORGOT_PASSWORD}>{t("login.forgotPassword")}</Link>
      }
    >
      <input
        name="email"
        type="email"
        placeholder={t("login.email")}
        required
      />
      <input
        name="password"
        type="password"
        placeholder={t("login.password")}
        required
      />
      {error && (
        <p>
          {(error as AxiosError<{ message: string }>)?.response?.data
            ?.message ?? t("common.error")}
        </p>
      )}
    </AuthForm>
  );
};
