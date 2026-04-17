import { useTranslation } from "react-i18next";
import { useCreateUser } from "../hooks/useCreateUser";
import type { CreateUserPayload } from "../types/User";
import { getApiError } from "@/shared/utils/getApiError";

interface CreateUserModalProps {
  onClose: () => void;
}

export const CreateUserModal = ({ onClose }: CreateUserModalProps) => {
  const { t } = useTranslation(["users", "common"]);
  const { mutate, isPending, error } = useCreateUser();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    const payload: CreateUserPayload = {
      firstName: form.get("firstName") as string,
      lastName: form.get("lastName") as string,
      email: form.get("email") as string,
      phone: form.get("phone") as string,
      role: form.get("role") as CreateUserPayload["role"],
      accessLevel: form.get("accessLevel") as CreateUserPayload["accessLevel"],
      merchantId: (form.get("merchantId") as string) || undefined,
    };

    mutate(payload, { onSuccess: onClose });
  };

  return (
    <dialog open>
      <h2>{t("create.title")}</h2>

      <form onSubmit={handleSubmit}>
        <input name="firstName" placeholder={t("table.firstName")} required />
        <input name="lastName" placeholder={t("table.lastName")} required />
        <input
          name="email"
          type="email"
          placeholder={t("table.email")}
          required
        />
        <input name="phone" placeholder={t("table.phone")} required />
        <input name="merchantId" placeholder={t("table.merchantId")} />

        <select name="role" required>
          <option value="admin">{t("role.admin")}</option>
          <option value="user">{t("role.user")}</option>
        </select>

        <select name="accessLevel" required>
          <option value="merchant">{t("accessLevel.merchant")}</option>
          <option value="branch">{t("accessLevel.branch")}</option>
        </select>

        <input name="merchantId" placeholder="Merchant ID" />

        {error && <p>{getApiError(error)}</p>}

        <button type="button" onClick={onClose}>
          {t("common:cancel")}
        </button>
        <button type="submit" disabled={isPending}>
          {isPending ? t("common.loadig") : t("actions.create")}{" "}
        </button>
      </form>
    </dialog>
  );
};
