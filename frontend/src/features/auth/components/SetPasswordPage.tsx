import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import { AuthForm } from "./AuthForm";
import { useSetPassword } from "../hooks/useSetPassword";
import { getApiErrorMessage } from "@/shared/utils/getApiErrorResponse";
import { ROUTES } from "@/router/routes";

export const SetPasswordPage = () => {
  const { t } = useTranslation("auth");
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const { mutate, isPending, error } = useSetPassword();
  const [validationError, setValidationError] = useState<string | null>(null);

  if (!token) {
    return <p>{t("setPassword.invalidLink")}</p>;
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setValidationError(null);

    const form = new FormData(e.currentTarget);
    const newPassword = form.get("newPassword") as string;
    const confirmPassword = form.get("confirmPassword") as string;

    if (newPassword !== confirmPassword) {
      setValidationError(t("setPassword.passwordMismatch"));
      return;
    }

    mutate(
      { token, newPassword },
      { onSuccess: () => navigate(ROUTES.LOGIN, { replace: true }) },
    );
  };

  return (
    <AuthForm
      title={t("setPassword.title")}
      subtitle={t("setPassword.subtitle")}
      onSubmit={handleSubmit}
      isLoading={isPending}
      submitLabel={t("setPassword.submit")}
    >
      <input
        name="newPassword"
        type="password"
        placeholder={t("setPassword.newPassword")}
        required
      />
      <input
        name="confirmPassword"
        type="password"
        placeholder={t("setPassword.confirmPassword")}
        required
      />
      {validationError && <p>{validationError}</p>}
      {error && <p>{getApiErrorMessage(error)}</p>}
    </AuthForm>
  );
};
