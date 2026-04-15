import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import { AuthForm } from "./AuthForm";
import { useResetPassword } from "../hooks/useResetPassword";
import { getApiErrorMessage } from "@/shared/utils/getApiErrorResponse";
import { ROUTES } from "@/router/routes";

export const ResetPasswordPage = () => {
  const { t } = useTranslation("auth");
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const { mutate, isPending, error } = useResetPassword();
  const [validationError, setValidationError] = useState<string | null>(null);

  if (!token) {
    return <p>{t("resetPassword.invalidLink")}</p>;
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setValidationError(null);

    const form = new FormData(e.currentTarget);
    const newPassword = form.get("newPassword") as string;
    const confirmPassword = form.get("confirmPassword") as string;

    if (newPassword !== confirmPassword) {
      setValidationError(t("resetPassword.passwordMismatch"));
      return;
    }

    mutate(
      { token, newPassword },
      { onSuccess: () => navigate(ROUTES.LOGIN, { replace: true }) },
    );
  };

  return (
    <AuthForm
      title={t("resetPassword.title")}
      subtitle={t("resetPassword.subtitle")}
      onSubmit={handleSubmit}
      isLoading={isPending}
      submitLabel={t("resetPassword.submit")}
    >
      <input
        name="newPassword"
        type="password"
        placeholder={t("resetPassword.newPassword")}
        required
      />
      <input
        name="confirmPassword"
        type="password"
        placeholder={t("resetPassword.confirmPassword")}
        required
      />
      {validationError && <p>{validationError}</p>}
      {error && <p>{getApiErrorMessage(error)}</p>}
    </AuthForm>
  );
};
