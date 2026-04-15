import { useTranslation } from "react-i18next";
import { useUpdateUser } from "../hooks/useUpdateUser";
import type { User, UpdateUserPayload } from "../types/User";
import { getApiErrorMessage } from "@/shared/utils/getApiErrorResponse";

interface EditUserModalProps {
  user: User;
  onClose: () => void;
}

export const EditUserModal = ({ user, onClose }: EditUserModalProps) => {
  const { t } = useTranslation("users");
  const { mutate, isPending, error } = useUpdateUser();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = new FormData(e.currentTarget);

    const payload: UpdateUserPayload = {
      firstName: (form.get("firstName") as string) || undefined,
      lastName: (form.get("lastName") as string) || undefined,
      email: (form.get("email") as string) || undefined,
      phone: (form.get("phone") as string) || undefined,
      role: (form.get("role") as UpdateUserPayload["role"]) || undefined,
      accessLevel:
        (form.get("accessLevel") as UpdateUserPayload["accessLevel"]) ||
        undefined,
      merchantId: (form.get("merchantId") as string) || undefined,
    };

    mutate({ id: user.id, payload }, { onSuccess: onClose });
  };

  return (
    <dialog open>
      <h2>{t("edit.title")}</h2>

      <form onSubmit={handleSubmit}>
        <input
          name="firstName"
          defaultValue={user.firstName}
          placeholder={t("table.firstName")}
        />
        <input
          name="lastName"
          defaultValue={user.lastName}
          placeholder={t("table.lastName")}
        />
        <input
          name="email"
          type="email"
          defaultValue={user.email}
          placeholder={t("table.email")}
        />
        <input
          name="phone"
          defaultValue={user.phone}
          placeholder={t("table.phone")}
        />

        <select name="role" defaultValue={user.role}>
          <option value="admin">{t("role.admin")}</option>
          <option value="user">{t("role.user")}</option>
        </select>

        <select name="accessLevel" defaultValue={user.accessLevel}>
          <option value="merchant">{t("accessLevel.merchant")}</option>
          <option value="branch">{t("accessLevel.branch")}</option>
        </select>

        <input
          name="merchantId"
          defaultValue={user.merchantId ?? ""}
          placeholder="Merchant ID"
        />

        {error && <p>{getApiErrorMessage(error) ?? t("errors.update")}</p>}

        <button type="button" onClick={onClose}>
          {t("common:cancel")}
        </button>
        <button type="submit" disabled={isPending}>
          {isPending ? "..." : t("actions.edit")}
        </button>
      </form>
    </dialog>
  );
};
