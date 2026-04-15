import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useUpdateUserStatus } from "../hooks/useUpdateUserStatus";
import type { User, UpdateStatusPayload } from "../types/User";
import { getApiErrorMessage } from "@/shared/utils/getApiErrorResponse";

interface UserStatusModalProps {
  user: User;
  onClose: () => void;
}

export const StatusModal = ({ user, onClose }: UserStatusModalProps) => {
  const { t } = useTranslation("users");
  const { mutate, isPending, error } = useUpdateUserStatus();
  const [status, setStatus] = useState<UpdateStatusPayload["status"]>(
    user.status,
  );

  const handleConfirm = () => {
    mutate({ id: user.id, payload: { status } }, { onSuccess: onClose });
  };

  return (
    <dialog open>
      <h2>{t("status.title")}</h2>
      <p>{t("status.message")}</p>
      <select
        value={status}
        onChange={(e) =>
          setStatus(e.target.value as UpdateStatusPayload["status"])
        }
      >
        <option value="active">{t("status.active")}</option>
        <option value="deactivated">{t("status.deactivated")}</option>
      </select>
      {error && <p>{getApiErrorMessage(error) ?? t("errors.status")}</p>}
      <button type="button" onClick={onClose}>
        {t("common:cancel")}
      </button>
      <button onClick={handleConfirm} disabled={isPending}>
        {isPending ? "..." : t("actions.changeStatus")}
      </button>
    </dialog>
  );
};
