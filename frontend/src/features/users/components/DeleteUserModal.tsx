import { useTranslation } from "react-i18next";
import { useDeleteUser } from "../hooks/useDeleteUser";
import type { User } from "../types/User";
import { getApiError } from "@/shared/utils/getApiError";

interface DeleteUserModalProps {
  user: User;
  onClose: () => void;
}

export const DeleteUserModal = ({ user, onClose }: DeleteUserModalProps) => {
  const { t } = useTranslation("users");
  const { mutate, isPending, error } = useDeleteUser();

  const handleDelete = () => {
    mutate(user.id, { onSuccess: onClose });
  };

  return (
    <dialog open>
      <h2>{t("delete.title")}</h2>
      <p>{t("delete.message")}</p>
      <p>
        {user.firstName} {user.lastName}
      </p>

      {error && <p>{getApiError(error)}</p>}

      <button type="button" onClick={onClose}>
        {t("common:cancel")}
      </button>
      <button onClick={handleDelete} disabled={isPending}>
        {isPending ? t("common.loadig") : t("actions.delete")}
      </button>
    </dialog>
  );
};
