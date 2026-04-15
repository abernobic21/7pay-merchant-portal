import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useChangePassword } from "../hooks/useChangePassword";
import { getApiErrorMessage } from "@/shared/utils/getApiErrorResponse";

interface ChangePasswordModalProps {
  onClose: () => void;
}

export const ChangePasswordModal = ({ onClose }: ChangePasswordModalProps) => {
  const { t } = useTranslation(["users", "common"]);
  const { mutate, isPending, error } = useChangePassword();
  const [validationError, setValidationError] = useState<string | null>(null);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setValidationError(null);

    const form = new FormData(e.currentTarget);
    const currentPassword = form.get("currentPassword") as string;
    const newPassword = form.get("newPassword") as string;
    const confirmPassword = form.get("confirmPassword") as string;

    if (newPassword !== confirmPassword) {
      setValidationError(t("profile.passwordMismatch"));
      return;
    }

    mutate({ currentPassword, newPassword }, { onSuccess: onClose });
  };

  return (
    <dialog open>
      <h2>{t("profile.changePassword")}</h2>

      <form onSubmit={handleSubmit}>
        <input
          name="currentPassword"
          type="password"
          placeholder={t("profile.currentPassword")}
          required
        />
        <input
          name="newPassword"
          type="password"
          placeholder={t("profile.newPassword")}
          required
        />
        <input
          name="confirmPassword"
          type="password"
          placeholder={t("profile.confirmPassword")}
          required
        />

        {validationError && <p>{validationError}</p>}
        {error && (
          <p>{getApiErrorMessage(error) ?? t("errors.changePassword")}</p>
        )}

        <button type="button" onClick={onClose}>
          {t("common:cancel")}
        </button>
        <button type="submit" disabled={isPending}>
          {isPending ? "..." : t("profile.changePassword")}
        </button>
      </form>
    </dialog>
  );
};
