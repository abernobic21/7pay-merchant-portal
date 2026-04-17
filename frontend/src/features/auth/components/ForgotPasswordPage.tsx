import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { AuthForm } from "./AuthForm";
import { useForgotPassword } from "../hooks/useForgotPassword";
import { ROUTES } from "@/router/routes";
import { getApiError } from "@/shared/utils/getApiError";

export const ForgotPasswordPage = () => {
  const { t } = useTranslation("auth");
  const { mutate, isPending, error, isSuccess } = useForgotPassword();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    mutate({ email: form.get("email") as string });
  };

  if (isSuccess) {
    return (
      <div>
        <p>{t("forgotPassword.successMessage")}</p>
        <Link to={ROUTES.LOGIN}>{t("forgotPassword.backToLogin")}</Link>
      </div>
    );
  }

  return (
    <AuthForm
      title={t("forgotPassword.title")}
      subtitle={t("forgotPassword.subtitle")}
      onSubmit={handleSubmit}
      isLoading={isPending}
      submitLabel={t("forgotPassword.submit")}
      footer={<Link to={ROUTES.LOGIN}>{t("forgotPassword.backToLogin")}</Link>}
    >
      <input
        name="email"
        type="email"
        placeholder={t("forgotPassword.email")}
        required
      />
      {error && <p>{getApiError(error)}</p>}
    </AuthForm>
  );
};
