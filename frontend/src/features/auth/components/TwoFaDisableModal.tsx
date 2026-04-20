import { useTranslation } from "react-i18next";
import { getApiError } from "@/shared/utils/getApiError";
import { useTwoFaDisable } from "../hooks/useTwoFaDisable";

interface Props {
  onClose: () => void;
}

export const TwoFaDisableModal = ({ onClose }: Props) => {
  const { t } = useTranslation("users");
  const mutation = useTwoFaDisable(onClose);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const password = new FormData(e.currentTarget).get("password") as string;
    mutation.mutate(password);
  };

  return (
    <div>
      <h2>{t("twoFa.disableTitle")}</h2>
      <p>{t("twoFa.disableWarning")}</p>
      <form onSubmit={handleSubmit}>
        <input
          name="password"
          type="password"
          placeholder={t("twoFa.passwordPlaceholder")}
          required
        />
        {mutation.error && (
          <p>{getApiError(mutation.error, "twoFa.disable")}</p>
        )}
        <button type="button" onClick={onClose}>
          {t("common:cancel")}
        </button>
        <button type="submit" disabled={mutation.isPending}>
          {t("twoFa.disableConfirm")}
        </button>
      </form>
    </div>
  );
};
