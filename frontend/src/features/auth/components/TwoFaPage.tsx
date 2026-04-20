import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { ROUTES } from "@/router/routes";
import { useTwoFa } from "../hooks/useTwoFa";
import { getApiError } from "@/shared/utils/getApiError";
import { AuthForm } from "./AuthForm";

export const TwoFaPage = () => {
  const { t } = useTranslation("auth");
  const navigate = useNavigate();
  const {
    useBackup,
    setUseBackup,
    preAuthToken,
    totpMutation,
    backupMutation,
  } = useTwoFa();

  useEffect(() => {
    if (!preAuthToken) {
      navigate(ROUTES.LOGIN, { replace: true });
    }
  }, [preAuthToken, navigate]);

  const activeMutation = useBackup ? backupMutation : totpMutation;

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    const code = form.get("code") as string;
    activeMutation.mutate(code);
  };

  return (
    <AuthForm
      title={t(useBackup ? "twoFa.backupTitle" : "twoFa.title")}
      onSubmit={handleSubmit}
      isLoading={activeMutation.isPending}
      submitLabel={t("twoFa.submit")}
      footer={
        <button type="button" onClick={() => setUseBackup((v) => !v)}>
          {t(useBackup ? "twoFa.useTotp" : "twoFa.useBackup")}
        </button>
      }
    >
      <input
        key={useBackup ? "backup" : "totp"}
        name="code"
        type="text"
        placeholder={t(
          useBackup ? "twoFa.backupPlaceholder" : "twoFa.codePlaceholder",
        )}
        autoComplete="one-time-code"
        autoFocus
        required
      />
      {activeMutation.error && (
        <p>
          {getApiError(
            activeMutation.error,
            useBackup ? "twoFa.backup" : "twoFa",
          )}
        </p>
      )}
    </AuthForm>
  );
};
